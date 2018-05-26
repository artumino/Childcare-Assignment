package com.polimi.childcare.shared.networking.requests.setters;

import com.polimi.childcare.shared.entities.RegistroPresenze;

public class SetRegistroPresenzeRequest extends SetEntityRequest<RegistroPresenze>
{
    public SetRegistroPresenzeRequest(RegistroPresenze entity, boolean toDelete, int oldHashCode) {
        super(entity, toDelete, oldHashCode);
    }

    public SetRegistroPresenzeRequest(RegistroPresenze entity, int oldHashCode) {
        super(entity, oldHashCode);
    }

    public SetRegistroPresenzeRequest(RegistroPresenze entity) {
        super(entity);
    }
}
