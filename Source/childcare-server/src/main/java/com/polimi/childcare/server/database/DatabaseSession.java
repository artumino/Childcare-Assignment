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
        streams = new JinqJPAStreamProvider(entityManagerFactory);
    }

    //@ensures (\result == true) <==> (sessionFactory != null)
    public Session getSession()
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
        if(sessionFactory != null) {
            Transaction tx = null;
            Integer ID = null;

            try (Session session = sessionFactory.openSession())
            {
                tx = session.beginTransaction();
                ID = (Integer) session.save(element);
                tx.commit();
            } catch (HibernateException e) {
                if (tx != null) tx.rollback();
                e.printStackTrace();
            }
            return ID;
        }
        else
            return null;
    }

    public <T> void update(T element) throws HibernateException
    {
        if(sessionFactory != null) {
            Transaction tx = null;

            try (Session session = sessionFactory.openSession())
            {
                tx = session.beginTransaction();
                session.update(element);
                tx.commit();
            } catch (HibernateException e) {
                if (tx != null) tx.rollback();
                throw e;
            }
        }
    }

    public <T> void insertOrUpdate(T element) throws HibernateException
    {
        if(sessionFactory != null) {
            Transaction tx = null;

            try (Session session = sessionFactory.openSession())
            {
                tx = session.beginTransaction();
                session.saveOrUpdate(element);
                tx.commit();
            } catch (HibernateException e) {
                if (tx != null) tx.rollback();
                throw e;
            }
        }
    }

    public <T> void delete(T element) throws HibernateException
    {
        if(sessionFactory != null) {
            Transaction tx = null;

            try (Session session = sessionFactory.openSession())
            {
                tx = session.beginTransaction();
                session.delete(element);
                tx.commit();
            } catch (HibernateException e) {
                if (tx != null) tx.rollback();
                throw e;
            }
        }
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

    public <T> JinqStream<T> query(Class<T> tClass)
    {
        if(streams == null)
            return null;

        //TODO: Crea problemi continuare a creare EntityManager?
        return streams.streamAll(entityManagerFactory.createEntityManager(), tClass);
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
}
