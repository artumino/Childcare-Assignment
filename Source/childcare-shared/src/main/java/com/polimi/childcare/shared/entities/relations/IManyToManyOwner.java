package com.polimi.childcare.shared.entities.relations;

import com.polimi.childcare.shared.entities.TransferableEntity;

import java.util.Set;

public interface IManyToManyOwner<T extends TransferableEntity, U extends TransferableEntity>
{
    U getItem();
    void addRelation(T item);
    void removeRelation(T item);
    Set<T> getUnmodifiableRelation();
    IManyToManyOwned<U,T> getInverse(T item);
}
