package com.polimi.childcare.server.database;


import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.jinq.jpa.JinqJPAStreamProvider;
import org.jinq.orm.stream.JinqStream;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

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
        if(sessionFactory != null)
            return;

        sessionFactory = new Configuration().configure().buildSessionFactory();
        entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
        streams = new JinqJPAStreamProvider(entityManagerFactory);
    }

    //@ensures (\result == true) <==> (sessionFactory != null)
    public Session getSession()
    {
        if(sessionFactory == null)
            return null;

        return sessionFactory.openSession();
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
            Integer ID = null;

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
            Transaction tx = null;
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
        //TODO: Crea problemi continuare a creare EntityManager?
        return streams.streamAll(entityManagerFactory.createEntityManager(), tClass);
    }

    public void close()
    {
        if(sessionFactory != null)
            sessionFactory.close();
        sessionFactory = null;
    }
}
