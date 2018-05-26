package com.polimi.childcare.shared.networking.requests.setters;

import com.polimi.childcare.shared.entities.MezzoDiTrasporto;

public class SetMezzoDiTrasportoRequest extends SetEntityRequest<MezzoDiTrasporto>
{
    public SetMezzoDiTrasportoRequest(MezzoDiTrasporto entity, boolean toDelete, int oldHashCode) {
        super(entity, toDelete, oldHashCode);
    }

    public SetMezzoDiTrasportoRequest(MezzoDiTrasporto entity, int oldHashCode) {
        super(entity, oldHashCode);
    }

    public SetMezzoDiTrasportoRequest(MezzoDiTrasporto entity) {
        super(entity);
    }
}
