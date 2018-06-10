package com.polimi.childcare.server.database;


import com.polimi.childcare.server.helper.DBHelper;
import org.hibernate.*;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.query.Query;

import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.stream.Stream;

public class DatabaseSession
{
    //region Singleton

    private static DatabaseSession instance;
    public static DatabaseSession getInstance() {
        return instance != null ? instance : (instance = new DatabaseSession());
    }

    //endregion

    private SessionFactory sessionFactory;

    /**
     * Effettua la connessione al DB ed inizializza le variabili per la corretta esecuzione di stream
     */
    public void setUp()
    {
        if(sessionFactory != null)
            return;

        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure() // configures settings from hibernate.cfg.xml
            .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            StandardServiceRegistryBuilder.destroy( registry );
            e.printStackTrace();
        }
    }

    /**
     * Crea una sessione per l'esecuzione diretta di stream sul DB
     * Ricordarsi di chiamare opportunamente close() al termine delle operazioni per evitare leak di connesioni al DB
     * @return Una nuova sessione verso il DB per l'esecuzione diretta di stream (senza i metodi helper di questa classe)
     */
    //@ensures (\result == true) <==> (sessionFactory != null)
    public Session openSession()
    {
        if(sessionFactory == null)
            return null;

        return sessionFactory.openSession();
    }

    /**
     * Ottiene l'attuale stringa di connessione dal DB
     * @return stringa di connessione al DB
     */
    public String getCurrentConnectionURL()
    {
        if(sessionFactory == null)
            return "";

        try {
            Connection connection = sessionFactory.getSessionFactoryOptions().getServiceRegistry().
                    getService(ConnectionProvider.class).getConnection();
            String url = connection.getMetaData().getURL();
            connection.close();
            return url;
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }

    //region Metodi per Entita

    /**
     * Inserisce un elemento nel DB e restituisce il suo ID
     * @param element Elemento da inserire nel DB
     * @param <T> Tipo di elemento da inserire nel DB
     * @return ID nel caso di corretto inserimento, null in caso contrario
     */
    public <T> Integer insert(T element)
    {
        //FIXME: Veramente brutto
        final Integer[] ID = {null};
        execute(session -> {
            ID[0] = session.insert(element);
            return true;
        });
        return ID[0];
    }

    /**
     * Inserisce gli elementi di una collezione in una trasazione(per migliori prestazioni)
     * @param elements Collezione di elementi da inserire
     * @param <T> Tipo di entità nella collezione
     */
    public <T> void insertAll(Collection<T> elements)
    {
        execute((session) -> {
            for(T element : elements)
                session.insert(element);
            return true;
        });
    }

    /**
     * Aggiorna un elemento nel DB
     * @param element Elemento da aggiornare
     * @param <T> Tipo di elemento da aggiornare
     * @throws HibernateException In caso di eccezioni e conflitti nell'aggiornamento
     */
    public <T> void update(T element) throws HibernateException
    {
        execute(session -> {
            //FIXME: Dopo il merge l'istanza di element è ancora consistente?
            session.update(session.contains(element) ? element : session.merge(element));
            return true;
        });
    }

    /**
     * Aggiorna o inserisce un elemento nel DB
     * @param element Elemento da inserire/aggiornare
     * @param <T> Tipo di elemento da aggiornare
     * @throws HibernateException In caso di eccezioni o conflitti in fase di aggiornamento
     */
    public <T> void insertOrUpdate(T element) throws HibernateException
    {
        execute(session -> {
            session.insertOrUpdate(session.contains(element) ? element : session.merge(element));
            return true;
        });
    }

    /**
     * Cancella un elemento dal DB
     * @param element Elemento da cancellare
     * @param <T> Tipo di elemento da cancellare
     * @throws HibernateException In caso di eccezioni durante la rimozione
     */
    public <T> void delete(T element) throws HibernateException
    {
        execute(session -> {
            session.delete(session.contains(element) ? element : session.merge(element));
            return true;
        });
    }

    //endregion

    //region Metodi per ID

    /**
     * Cerca di ottenere un elemento dal database
     * @param tClass Classe dell'entità che ci si aspetta
     * @param ID ID dell'elemento da selezionare nel DB
     * @param eager Se true ritorna l'elemento con tutte le relazioni di prima generazione inizializzate, false invece non li carica (null o proxy di Hibernate)
     * @param <T> Tipo elemento da selezionare
     * @return null in caso di errori, oggetto di tipo T corrispondende all'ID fornito in caso contrario
     */
    public <T> T getByID(Class<T> tClass, Integer ID, boolean eager)
    {
        if(sessionFactory != null) {
            T entity = null;

            try (Session session = sessionFactory.openSession())
            {
                entity = session.get(tClass, ID);
                if(eager && entity != null)
                    DBHelper.recursiveObjectInitialize(entity);
            } catch (HibernateException e) {
                e.printStackTrace();
            }
            return entity;
        }
        else
            return null;
    }

    /**
     * @see DatabaseSession#getByID(Class, Integer, boolean) chiamato con parametro eager=false
     * @param tClass Classe dell'entità che ci si aspetta
     * @param ID ID dell'elemento da selezionare nel DB
     * @param <T> Tipo elemento da selezionare
     * @return null in caso di errori, oggetto di tipo T corrispondende all'ID fornito in caso contrario
     */
    public <T> T getByID(Class<T> tClass, Integer ID)
    {
        return getByID(tClass, ID, false);
    }

    public <T> void deleteByID(Class<T> tClass, Integer ID)
    {
        T entity = getByID(tClass, ID);
        if(entity != null)
            delete(entity);
    }

    //endregion

    /**
     * Esegue una stream di tipo JINQ sul DB
     * @param tClass Classe della tabella delle entità su cui effettuare la stream
     * @param session Sessione del DB su cui effettuare la stream
     * @param <T> Tipo delle entità di ritorno
     * @return Ritorna uno stream JINQ filtrabile tramite i relativi metodi
     */
    public <T> Stream<T> stream(Class<T> tClass, Session session)
    {
        if(session == null)
            return null;

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<T> query = criteriaBuilder.createQuery(tClass);
        query.from(tClass);
        return session.createQuery(query).stream();
    }

    /**
     * Esegue una transazione sul DB
     * @param execution Codice della transazione da eseguire, in base al risultato della transazione effettua automaticamente il commit() o rollback()
     * @return null in caso di commit, Throwable in caso di errore
     */
    public Throwable execute(IDatabaseExecution execution)
    {
        if(sessionFactory == null)
            return new Throwable("No session factory");

        Transaction tx = null;
        try(Session session = sessionFactory.openSession())
        {
            tx = session.beginTransaction();
            DatabaseSessionInstance exec = new DatabaseSessionInstance(session, tx);
            if(execution.execute(exec))
                tx.commit();
            else
            {
                try {
                    tx.rollback();
                }
                catch (Throwable ex)
                {
                    ex.printStackTrace();
                }
                return new Throwable("Error during transaction");
            }
        }catch (PersistenceException ex)
        {
            ex.printStackTrace();
            try {
                if (tx != null) tx.rollback();
            } catch (Throwable thr)
            {
                thr.printStackTrace();
            }
            return ex;
        }

        return null;
    }

    /**
     * Termina la connessione al DB, interrompe tutte le attuali sessioni aperte
     */
    public void close()
    {
        if(sessionFactory != null)
            sessionFactory.close();
        sessionFactory = null;
    }

    /**
     * Innerclass utilizzata da execute() per create un contesto di esecuzione in una transazione del DB
     * Fornisce accesso ad ogni elemento di una sessione nel DB ma all'interno di una transazione
     * @see DatabaseSession#execute(IDatabaseExecution) per maggiori informazioni
     */
    public class DatabaseSessionInstance
    {
        private Session session;
        private Transaction transaction;

        DatabaseSessionInstance(Session session, Transaction transaction)
        {
            this.session = session;
            this.transaction = transaction;
        }

        //region Metodi per Entità

        public <T> Integer insert(T element)
        {
            Integer ID = null;
            try
            {
                ID = (Integer) session.save(element);
            } catch (HibernateException e) {
                e.printStackTrace();
            }
            return ID;
        }

        public Session getSession()
        {
            return session;
        }

        public <T> void update(T element) throws HibernateException
        {
            if(!contains(element))
                element = merge(element);
            session.update(element);
        }

        public <T> void insertOrUpdate(T element) throws HibernateException
        {
            if(!contains(element))
                element = merge(element);
            session.saveOrUpdate(element);
        }

        public <T> void delete(T element) throws HibernateException
        {
            if(!contains(element))
                element = merge(element);
            session.delete(element);
        }

        public <T> void deleteAll(Collection<T> elements) throws HibernateException
        {
            for(T element : elements)
                session.delete(element);
        }

        public <T> boolean contains(T element) throws HibernateException
        {
            return session.contains(element);
        }

        public <T> T merge(T element) throws HibernateException
        {
            Object obj = session.merge(element);
            if(obj != null && obj.getClass().isInstance(element))
                return (T)obj;
            return null;
        }

        //endregion

        //region Metodi per ID


        public <T> T getByID(Class<T> tClass, Integer ID, boolean eager)
        {
            T result = session.get(tClass, ID);
            if(eager)
                DBHelper.recursiveObjectInitialize(result);
            return result;
        }

        public <T> T getByID(Class<T> tClass, Integer ID)
        {
            return getByID(tClass, ID, false);
        }

        public <T> void deleteByID(Class<T> tClass, Integer ID)
        {
            T entity = getByID(tClass, ID);
            if(entity != null)
                delete(entity);
        }

        //endregion

        public <T> Stream<T> stream(Class<T> tClass)
        {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<T> query = criteriaBuilder.createQuery(tClass);
            query.from(tClass);
            return session.createQuery(query).stream();
        }
    }

    /**
     * Classe per l'esecuzione di transazioni sul DB
     */
    public interface IDatabaseExecution
    {
        boolean execute(DatabaseSessionInstance execution);
    }
}
