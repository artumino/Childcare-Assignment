package com.polimi.childcare.server.database;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DatabaseSession
{
    private static DatabaseSession instance;
    private SessionFactory sessionFactory;

    public static DatabaseSession getInstance() {
        return instance != null ? instance : (instance = new DatabaseSession());
    }

    public void setUp()
    {
        if(sessionFactory != null)
            return;

        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    //@ensures (\result == true) <==> (sessionFactory != null)
    public Session getSession()
    {
        if(sessionFactory == null)
            return null;

        return sessionFactory.openSession();
    }

    public void close()
    {
        if(sessionFactory != null)
            sessionFactory.close();
        sessionFactory = null;
    }
}
