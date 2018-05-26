package com.polimi.childcare.shared.networking.requests.setters;

import com.polimi.childcare.shared.entities.NumeroTelefono;

public class SetNumeroDiTelefonoRequest extends SetEntityRequest<NumeroTelefono>
{
    public SetNumeroDiTelefonoRequest(NumeroTelefono entity, boolean toDelete, int oldHashCode) {
        super(entity, toDelete, oldHashCode);
    }

    public SetNumeroDiTelefonoRequest(NumeroTelefono entity, int oldHashCode) {
        super(entity, oldHashCode);
    }

    public SetNumeroDiTelefonoRequest(NumeroTelefono entity) {
        super(entity);
    }
}
