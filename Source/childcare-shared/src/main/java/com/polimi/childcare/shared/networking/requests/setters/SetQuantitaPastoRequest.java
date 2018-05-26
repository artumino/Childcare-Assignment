package com.polimi.childcare.shared.networking.requests.setters;

import com.polimi.childcare.shared.entities.QuantitaPasto;

public class SetQuantitaPastoRequest extends SetEntityRequest<QuantitaPasto>
{
    public SetQuantitaPastoRequest(QuantitaPasto entity, boolean toDelete, int oldHashCode) {
        super(entity, toDelete, oldHashCode);
    }

    public SetQuantitaPastoRequest(QuantitaPasto entity, int oldHashCode) {
        super(entity, oldHashCode);
    }

    public SetQuantitaPastoRequest(QuantitaPasto entity) {
        super(entity);
    }
}
