package com.polimi.childcare.shared.networking.requests.setters;

import com.polimi.childcare.shared.entities.Diagnosi;

public class SetDiagnosiRequest extends SetEntityRequest<Diagnosi>
{
    public SetDiagnosiRequest(Diagnosi entity, boolean toDelete, int oldHashCode) {
        super(entity, toDelete, oldHashCode);
    }

    public SetDiagnosiRequest(Diagnosi entity, int oldHashCode) {
        super(entity, oldHashCode);
    }

    public SetDiagnosiRequest(Diagnosi entity) {
        super(entity);
    }
}
