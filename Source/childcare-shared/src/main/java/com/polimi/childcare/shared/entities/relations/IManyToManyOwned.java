package com.polimi.childcare.shared.entities.relations;

import com.polimi.childcare.shared.entities.TransferableEntity;

import java.util.Set;

public interface IManyToManyOwned<T extends TransferableEntity, U extends TransferableEntity>
{
    U getItem();
    void unsafeAddRelation(T item);
    void unsafeRemoveRelation(T item);
    Set<T> getUnmodifiableRelation();
    IManyToManyOwner<U,T> getInverse(T item);
}
