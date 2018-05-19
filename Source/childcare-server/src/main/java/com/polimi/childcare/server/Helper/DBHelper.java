package com.polimi.childcare.server.Helper;

import com.sun.org.apache.xml.internal.dtm.ref.EmptyIterator;
import org.hibernate.Hibernate;
import org.jinq.orm.stream.JinqStream;

import java.util.*;

public class DBHelper
{
    public static <T> void objectInizialize(T object)
    {
        if(!Hibernate.isInitialized(object))
            Hibernate.initialize(object);
    }

    public static void filterAdd(JinqStream query, HashMap<JinqStream.CollectComparable, Boolean> param, List<JinqStream.Where> filters) throws Exception
    {
        for (JinqStream.Where entry : filters)
        {
            query = query.where(entry);
        }

        Iterator it = param.entrySet().iterator();

        if(!it.hasNext())
            throw new Exception("Ordinamento vuoto!");

        Map.Entry entry = (Map.Entry)it.next();

        if((Boolean)entry.getValue())
            query = query.sortedBy((JinqStream.CollectComparable)entry.getKey());

        else
            query = query.sortedDescendingBy((JinqStream.CollectComparable)entry.getKey());

    }

}
