package com.polimi.childcare.shared.networking.requests.setters;

import com.polimi.childcare.shared.entities.Contatto;

public class SetContattoRequest extends SetEntityRequest<Contatto>
{
    public SetContattoRequest(Contatto entity, boolean toDelete, int oldHashCode) {
        super(entity, toDelete, oldHashCode);
    }

    public SetContattoRequest(Contatto entity, int oldHashCode) {
        super(entity, oldHashCode);
    }

    public SetContattoRequest(Contatto entity) {
        super(entity);
    }
}
