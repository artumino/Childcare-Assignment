package com.polimi.childcare.server.helper;

import org.hibernate.Hibernate;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class DBHelper
{
    public static <T> T objectInitialize(T object)
    {

        if(!Hibernate.isInitialized(object))
            Hibernate.initialize(object);
        T unproxiedobj = (T)Hibernate.unproxy(object);
        return unproxiedobj;
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
                field.set(object, objectInitialize(fieldValue));

                //Ben tornato private :)
                if(!normallyAccessible)
                    field.setAccessible(false);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static <T> void recursiveIterableIntitialize(Iterable<T> iterable)
    {
        for (T item : iterable) {
            recursiveObjectInitialize(item);
        }
    }

    public static <T> void filterAdd(Stream<T> query, List<Comparator<T>> param, List<Predicate<T>> filters)
    {
        if(filters != null)
        {
            for (Predicate<T> entry : filters) {
                query = query.filter(entry);
            }
        }

        if(param != null)
        {
            for (Comparator<T> entry : param) {
                query = query.sorted(entry);
            }
        }
    }

}
