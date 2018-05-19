package com.polimi.childcare.server.Helper;

import org.hibernate.Hibernate;

public class DBHelper
{
    public static <IT, T extends Iterable<IT>> void iterableInizialize(T list)
    {
        for(IT item : list)
        {
            if(!Hibernate.isInitialized(item))
                Hibernate.initialize(item);
        }
    }

    public static <T> void objectInizialize(T object)
    {
        if(!Hibernate.isInitialized(object))
            Hibernate.initialize(object);
    }
}
