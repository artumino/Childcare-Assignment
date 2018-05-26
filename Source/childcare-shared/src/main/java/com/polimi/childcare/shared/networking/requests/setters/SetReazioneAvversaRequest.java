package com.polimi.childcare.shared.networking.requests.setters;

import com.polimi.childcare.shared.entities.ReazioneAvversa;

public class SetReazioneAvversaRequest extends SetEntityRequest<ReazioneAvversa>
{
    public SetReazioneAvversaRequest(ReazioneAvversa entity, boolean toDelete, int oldHashCode) {
        super(entity, toDelete, oldHashCode);
    }

    public SetReazioneAvversaRequest(ReazioneAvversa entity, int oldHashCode) {
        super(entity, oldHashCode);
    }

    public SetReazioneAvversaRequest(ReazioneAvversa entity) {
        super(entity);
    }
}
