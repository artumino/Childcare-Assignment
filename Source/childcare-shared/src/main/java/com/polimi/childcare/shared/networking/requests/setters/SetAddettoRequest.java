package com.polimi.childcare.shared.networking.requests.setters;

import com.polimi.childcare.shared.entities.Addetto;

public class SetAddettoRequest extends SetEntityRequest<Addetto>
{
    public SetAddettoRequest(Addetto entity, boolean toDelete, int oldHashCode) {
        super(entity, toDelete, oldHashCode);
    }

    public SetAddettoRequest(Addetto entity, int oldHashCode) {
        super(entity, oldHashCode);
    }

    public SetAddettoRequest(Addetto entity) {
        super(entity);
    }
}
