package com.polimi.childcare.shared.utils;

import java.util.*;

public class EntitiesHelper
{
    public static <T> Set <T> unmodifiableListReturn(Set<T> set)
    {
        Set<T> list = new HashSet<>(set);
        Collections.unmodifiableSet(list);
        return list;
    }
}
