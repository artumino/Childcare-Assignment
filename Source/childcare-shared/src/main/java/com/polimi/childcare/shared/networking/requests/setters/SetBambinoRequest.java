package com.polimi.childcare.shared.networking.requests.setters;

import com.polimi.childcare.shared.entities.Bambino;

public class SetBambinoRequest extends SetEntityRequest<Bambino>
{
    public SetBambinoRequest(Bambino entity, boolean toDelete, int oldHashCode) {
        super(entity, toDelete, oldHashCode);
    }

    public SetBambinoRequest(Bambino entity, int oldHashCode) {
        super(entity, oldHashCode);
    }

    public SetBambinoRequest(Bambino entity) {
        super(entity);
    }
}
