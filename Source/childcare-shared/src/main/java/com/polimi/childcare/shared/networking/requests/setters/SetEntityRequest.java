package com.polimi.childcare.shared.networking.requests.setters;

import com.polimi.childcare.shared.entities.TransferableEntity;
import com.polimi.childcare.shared.networking.requests.BaseRequest;

/**
 * Richiesta di modifica/inserimento/cancellazione di una entità generica
 * @param <T> Tipo di entità da modificare
 */
public class SetEntityRequest<T extends TransferableEntity> extends BaseRequest
{
    private T entity;
    private boolean toDelete;
    private int oldHashCode;

    public SetEntityRequest(T entity, boolean toDelete, int oldHashCode) {
        this.entity = entity;
        this.toDelete = toDelete;
        this.oldHashCode = oldHashCode;
    }

    public SetEntityRequest(T entity, int oldHashCode) {
        this.entity = entity;
        this.toDelete = false;
        this.oldHashCode = oldHashCode;
    }

    public SetEntityRequest(T entity) {
        this.entity = entity;
        this.oldHashCode = 0;
        this.toDelete = false;
    }

    public T getEntity() {
        return entity;
    }

    public boolean isToDelete() {
        return toDelete;
    }

    public int getOldHashCode() { return oldHashCode; }
}
