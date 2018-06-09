package com.polimi.childcare.server.database.dao;

import com.polimi.childcare.server.database.DatabaseSession;
import org.hibernate.Hibernate;

public abstract class HibernateDao<T> implements ICommonDao<T>
{
    protected DatabaseSession.DatabaseSessionInstance sessionInstance;

    public HibernateDao(DatabaseSession.DatabaseSessionInstance sessionInstance)
    {
        this.sessionInstance = sessionInstance;
    }
}
