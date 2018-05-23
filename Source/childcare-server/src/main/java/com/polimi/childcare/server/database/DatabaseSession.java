package com.polimi.childcare.server.database;


import com.polimi.childcare.server.helper.DBHelper;
import org.hibernate.*;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.jinq.jpa.JinqJPAStreamProvider;
import org.jinq.orm.stream.JinqStream;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

public class DatabaseSession
{
    //region Singleton

    private static DatabaseSession instance;
    public static DatabaseSession getInstance() {
        return instance != null ? instance : (instance = new DatabaseSession());
    }

    //endregion

    private SessionFactory sessionFactory;
    private EntityManagerFactory entityManagerFactory;
    private JinqJPAStreamProvider streams;

    /**
     * Effettua la connessione al DB ed inizializza le variabili per la corretta esecuzione di query
     */
    public void setUp()
    {
        if(entityManagerFactory != null)
            return;

        entityManagerFactory = Persistence.createEntityManagerFactory("hbr");
        sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        streams = new JinqJPAStreamProvider(sessionFactory);
    }

    /**
     * Crea una sessione per l'esecuzione diretta di query sul DB
     * @implNote Ricordarsi di chiamare opportunamente close() al termine delle operazioni per evitare leak di connesioni al DB
     * @return Una nuova sessione verso il DB per l'esecuzione diretta di query (senza i metodi helper di questa classe)
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
                if(eager)
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
     * Esegue una query di tipo JINQ sul DB
     * @param tClass Classe della tabella delle entità su cui effettuare la query
     * @param session Sessione del DB su cui effettuare la query
     * @param <T> Tipo delle entità di ritorno
     * @return Ritorna uno stream JINQ filtrabile tramite i relativi metodi
     */
    public <T> JinqStream<T> query(Class<T> tClass, Session session)
    {
        if(streams == null)
            return null;

        return streams.streamAll(session, tClass);
    }

    /**
     * Esegue una transazione sul DB
     * @param execution Codice della transazione da eseguire, in base al risultato della transazione effettua automaticamente il commit() o rollback()
     * @return true in caso di commit, false in caso di rollback()
     */
    public boolean execute(IDatabaseExecution execution)
    {
        if(sessionFactory == null)
            return false;

        Transaction tx = null;
        try(Session session = sessionFactory.openSession())
        {
            tx = session.beginTransaction();
            DatabaseSessionInstance exec = new DatabaseSessionInstance(session, streams, tx);
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
                return false;
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
            return false;
        }

        return true;
    }

    /**
     * Termina la connessione al DB, interrompe tutte le attuali sessioni aperte
     */
    public void close()
    {
        if(sessionFactory != null)
            sessionFactory.close();
        sessionFactory = null;

        streams = null;

        if(entityManagerFactory != null)
            entityManagerFactory.close();
        entityManagerFactory = null;
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
        private JinqJPAStreamProvider streams;

        DatabaseSessionInstance(Session session, JinqJPAStreamProvider streams, Transaction transaction)
        {
            this.session = session;
            this.streams = streams;
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
            session.update(element);
        }

        public <T> void insertOrUpdate(T element) throws HibernateException
        {
            session.saveOrUpdate(element);
        }

        public <T> void delete(T element) throws HibernateException
        {
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

        public <T> JinqStream<T> query(Class<T> tClass)
        {
            if(streams == null)
                return null;

            return streams.streamAll(session, tClass);
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
