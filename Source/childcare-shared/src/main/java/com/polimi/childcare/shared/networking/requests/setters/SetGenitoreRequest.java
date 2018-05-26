package com.polimi.childcare.shared.networking.requests.setters;

import com.polimi.childcare.shared.entities.Genitore;

public class SetGenitoreRequest extends SetEntityRequest<Genitore>
{
    public SetGenitoreRequest(Genitore entity, boolean toDelete, int oldHashCode) {
        super(entity, toDelete, oldHashCode);
    }

    public SetGenitoreRequest(Genitore entity, int oldHashCode) {
        super(entity, oldHashCode);
    }

    public SetGenitoreRequest(Genitore entity) {
        super(entity);
    }
}
