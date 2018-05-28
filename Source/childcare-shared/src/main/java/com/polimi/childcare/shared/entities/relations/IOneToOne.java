package com.polimi.childcare.shared.entities.relations;

public interface IOneToOne<T>
{
    void setRelation(T item);
    T getRelation();
}
