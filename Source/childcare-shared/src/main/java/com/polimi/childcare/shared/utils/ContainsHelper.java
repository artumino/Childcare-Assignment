package com.polimi.childcare.shared.utils;

import com.polimi.childcare.shared.entities.IIdentificable;

import java.util.Collection;
import java.util.Comparator;
import java.util.function.Predicate;

public class ContainsHelper
{
    public static <T> boolean contains(Collection<T> list, T item, Comparator<? super T> comparator) {
        return list.stream().map(t -> comparator.compare(t, item))
                .anyMatch(Predicate.isEqual(0));
    }

    public static <T extends IIdentificable> boolean containsID(Collection<T> list, T item) {
        return list.stream().map(t -> Integer.compare(t.getID(), item.getID()))
                .anyMatch(Predicate.isEqual(0));
    }

    public static <T> boolean containsHashCode(Collection<T> list, T item) {
        return list.stream().map(t -> Integer.compare(t.hashCode(), item.hashCode()))
                .anyMatch(Predicate.isEqual(0));
    }
}
