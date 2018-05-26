package com.polimi.childcare.shared.networking.requests.setters;

import com.polimi.childcare.shared.entities.Pasto;

public class SetPastiRequest extends SetEntityRequest<Pasto>
{
    public SetPastiRequest(Pasto entity, boolean toDelete, int oldHashCode) {
        super(entity, toDelete, oldHashCode);
    }

    public SetPastiRequest(Pasto entity, int oldHashCode) {
        super(entity, oldHashCode);
    }

    public SetPastiRequest(Pasto entity) {
        super(entity);
    }
}
