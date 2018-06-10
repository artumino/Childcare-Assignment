package com.polimi.childcare.server.database.dao;

import com.polimi.childcare.server.database.DatabaseSession;
import org.hibernate.Hibernate;

/**
 * Implementazione dell'interfaccia {@code ICommonDao<T>} per Hibernate
 * Permette quindi l'accesso alla sessione di Hibernate attuale
 * @param <T> Tipo di entità a cui questo Dao fa riferimento
 */
public abstract class HibernateDao<T> implements ICommonDao<T>
{
    protected DatabaseSession.DatabaseSessionInstance sessionInstance;

    public HibernateDao(DatabaseSession.DatabaseSessionInstance sessionInstance)
    {
        this.sessionInstance = sessionInstance;
    }
}
