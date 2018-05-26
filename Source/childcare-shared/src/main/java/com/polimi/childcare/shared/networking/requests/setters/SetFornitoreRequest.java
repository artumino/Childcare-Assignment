package com.polimi.childcare.shared.networking.requests.setters;

import com.polimi.childcare.shared.entities.Fornitore;

public class SetFornitoreRequest extends SetEntityRequest<Fornitore>
{
    public SetFornitoreRequest(Fornitore entity, boolean toDelete, int oldHashCode) {
        super(entity, toDelete, oldHashCode);
    }

    public SetFornitoreRequest(Fornitore entity, int oldHashCode) {
        super(entity, oldHashCode);
    }

    public SetFornitoreRequest(Fornitore entity) {
        super(entity);
    }
}
