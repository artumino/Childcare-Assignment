package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.shared.entities.Pasto;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;

public class PastiRequestHandlerSet extends GenericDaoSetEntityRequestHandler<SetEntityRequest<Pasto>, Pasto>
{

    @Override
    protected Class<Pasto> getQueryClass() {
        return Pasto.class;
    }
}