package com.polimi.childcare.server.database;


import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.jinq.jpa.JinqJPAStreamProvider;
import org.jinq.orm.stream.JinqStream;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Connection;
import java.sql.SQLException;

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

    public void setUp()
    {
        if(entityManagerFactory != null)
            return;

        entityManagerFactory = Persistence.createEntityManagerFactory("hbr");
        sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        streams = new JinqJPAStreamProvider(sessionFactory);
    }

    //@ensures (\result == true) <==> (sessionFactory != null)
    public Session openSession()
    {
        if(sessionFactory == null)
            return null;

        return sessionFactory.openSession();
    }

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

    public <T> void update(T element) throws HibernateException
    {
        execute(session -> {
            //FIXME: Dopo il merge l'istanza di element è ancora consistente?
            session.update(session.contains(element) ? element : session.merge(element));
            return true;
        });
    }

    public <T> void insertOrUpdate(T element) throws HibernateException
    {
        execute(session -> {
            session.insertOrUpdate(session.contains(element) ? element : session.merge(element));
            return true;
        });
    }

    public <T> void delete(T element) throws HibernateException
    {
        execute(session -> {
            session.delete(session.contains(element) ? element : session.merge(element));
            return true;
        });
    }

    //endregion

    //region Metodi per ID

    public <T> T getByID(Class<T> tClass, Integer ID)
    {
        if(sessionFactory != null) {
            T entity = null;

            try (Session session = sessionFactory.openSession())
            {
                entity = session.get(tClass, ID);
            } catch (HibernateException e) {
                e.printStackTrace();
            }
            return entity;
        }
        else
            return null;
    }

    public <T> void deleteByID(Class<T> tClass, Integer ID)
    {
        T entity = getByID(tClass, ID);
        if(entity != null)
            delete(entity);
    }

    //endregion

    public <T> JinqStream<T> query(Class<T> tClass, Session session)
    {
        if(streams == null)
            return null;

        return streams.streamAll(session, tClass);
    }

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
                tx.rollback();
                return false;
            }

        }catch (HibernateException ex)
        {
            ex.printStackTrace();
            if(tx != null) tx.rollback();
            return false;
        }

        return true;
    }

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

        public <T> T getByID(Class<T> tClass, Integer ID)
        {
            return session.get(tClass, ID);
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

    public interface IDatabaseExecution
    {
        boolean execute(DatabaseSessionInstance execution);
    }
}
