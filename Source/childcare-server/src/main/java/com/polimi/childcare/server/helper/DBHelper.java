package com.polimi.childcare.server.helper;

import org.hibernate.Hibernate;
import org.jinq.orm.stream.JinqStream;

import java.lang.reflect.Field;
import java.util.*;

public class DBHelper
{
    public static <T> void objectInitialize(T object)
    {
        if(!Hibernate.isInitialized(object))
            Hibernate.initialize(object);
    }

    public static <T> void recursiveObjectInitialize(T object)
    {
        //Ottengo tutti i possibili field
        List<Field> fieldList = new ArrayList<>(Arrays.asList(object.getClass().getDeclaredFields()));

        Class tmpClass = object.getClass().getSuperclass();
        while (tmpClass != null) {
            fieldList.addAll(Arrays.asList(tmpClass.getDeclaredFields()));
            tmpClass = tmpClass.getSuperclass();
        }

        //Cerco di inizializzare ogni field
        boolean normallyAccessible = false;
        for(Field field : fieldList)
        {
            try {
                //Addio Private :(
                if(!(normallyAccessible = field.isAccessible()))
                    field.setAccessible(true);

                Object fieldValue = field.get(object);
                objectInitialize(fieldValue);

                //Ben tornato private :)
                if(!normallyAccessible)
                    field.setAccessible(false);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static void filterAdd(JinqStream query, HashMap<JinqStream.CollectComparable, Boolean> param, List<JinqStream.Where> filters) throws Exception
    {
        if(filters != null)
        {
            for (JinqStream.Where entry : filters) {
                query = query.where(entry);
            }
        }

        if(param != null)
        {
            Iterator it = param.entrySet().iterator();

            if (!it.hasNext())
                return; // throw new Exception("Ordinamento vuoto!"); (Non ha senso ritornare un'eccezione, se param è null non ci sono ordinamenti

            Map.Entry entry = (Map.Entry) it.next();

            if ((Boolean) entry.getValue())
                query = query.sortedBy((JinqStream.CollectComparable) entry.getKey());

            else
                query = query.sortedDescendingBy((JinqStream.CollectComparable) entry.getKey());
        }

    }

}
