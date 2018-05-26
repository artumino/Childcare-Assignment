package com.polimi.childcare.shared.networking.requests.setters;

import com.polimi.childcare.shared.entities.Pediatra;

public class SetPediatraRequest extends SetEntityRequest<Pediatra>
{
    public SetPediatraRequest(Pediatra entity, boolean toDelete, int oldHashCode) {
        super(entity, toDelete, oldHashCode);
    }

    public SetPediatraRequest(Pediatra entity, int oldHashCode) {
        super(entity, oldHashCode);
    }

    public SetPediatraRequest(Pediatra entity) {
        super(entity);
    }
}
