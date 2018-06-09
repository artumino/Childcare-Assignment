package com.polimi.childcare.server.database.dao;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.database.dao.implementations.GruppoDaoImpl;
import com.polimi.childcare.shared.entities.Gruppo;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class DaoFactory
{
    //region Singleton

    private static DaoFactory instance;
    public static DaoFactory getInstance() {
        return instance != null ? instance : (instance = new DaoFactory());
    }

    //endregion

    private HashMap<Class,Class<? extends ICommonDao>> daoMap;

    public DaoFactory()
    {
        daoMap = new HashMap<>();

        daoMap.put(Gruppo.class, GruppoDaoImpl.class);
    }

    public <T> ICommonDao<T> getDao(Class<T> tClass, DatabaseSession.DatabaseSessionInstance sessionInstance)
    {
        try {
            return daoMap.get(tClass).getConstructor(DatabaseSession.DatabaseSessionInstance.class).newInstance(sessionInstance);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
