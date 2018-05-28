package com.polimi.childcare.shared.entities.relations;

import com.polimi.childcare.shared.entities.TransferableEntity;

import java.util.Set;

public interface IOneToMany<T extends TransferableEntity,U extends TransferableEntity>
{
    U getItem();
    void unsafeAddRelation(T item);
    void unsafeRemoveRelation(T item);
    Set<T> getUnmodifiableRelation();
    IManyToOne<U,T> getInverse(T item);
}
