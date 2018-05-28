package com.polimi.childcare.shared.entities.relations;

import com.polimi.childcare.shared.entities.TransferableEntity;

public interface IManyToOne<T extends TransferableEntity,U extends TransferableEntity>
{
    U getItem();
    void setRelation(T item);
    T getRelation();
    IOneToMany<U,T> getInverse(T item);
}
