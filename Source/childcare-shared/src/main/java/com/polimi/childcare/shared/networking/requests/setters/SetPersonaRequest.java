package com.polimi.childcare.shared.networking.requests.setters;

import com.polimi.childcare.shared.entities.Persona;

public class SetPersonaRequest extends SetEntityRequest<Persona>
{
    public SetPersonaRequest(Persona entity, boolean toDelete, int oldHashCode) {
        super(entity, toDelete, oldHashCode);
    }

    public SetPersonaRequest(Persona entity, int oldHashCode) {
        super(entity, oldHashCode);
    }

    public SetPersonaRequest(Persona entity) {
        super(entity);
    }
}
