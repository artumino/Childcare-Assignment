package com.polimi.childcare.shared.networking.requests.setters;

import com.polimi.childcare.shared.entities.PianoViaggi;

public class SetPianoViaggiRequest extends SetEntityRequest<PianoViaggi>
{
    public SetPianoViaggiRequest(PianoViaggi entity, boolean toDelete, int oldHashCode) {
        super(entity, toDelete, oldHashCode);
    }

    public SetPianoViaggiRequest(PianoViaggi entity, int oldHashCode) {
        super(entity, oldHashCode);
    }

    public SetPianoViaggiRequest(PianoViaggi entity) {
        super(entity);
    }
}
