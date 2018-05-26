package com.polimi.childcare.shared.networking.requests.setters;

import com.polimi.childcare.shared.entities.Gruppo;

public class SetGruppoRequest extends SetEntityRequest<Gruppo>
{
    public SetGruppoRequest(Gruppo entity, boolean toDelete, int oldHashCode) {
        super(entity, toDelete, oldHashCode);
    }

    public SetGruppoRequest(Gruppo entity, int oldHashCode) {
        super(entity, oldHashCode);
    }

    public SetGruppoRequest(Gruppo entity) {
        super(entity);
    }
}
