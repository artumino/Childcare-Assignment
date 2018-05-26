package com.polimi.childcare.shared.networking.requests.setters;

import com.polimi.childcare.shared.entities.Gita;

public class SetGitaRequest extends SetEntityRequest<Gita>
{
    public SetGitaRequest(Gita entity, boolean toDelete, int oldHashCode) {
        super(entity, toDelete, oldHashCode);
    }

    public SetGitaRequest(Gita entity, int oldHashCode) {
        super(entity, oldHashCode);
    }

    public SetGitaRequest(Gita entity) {
        super(entity);
    }
}
