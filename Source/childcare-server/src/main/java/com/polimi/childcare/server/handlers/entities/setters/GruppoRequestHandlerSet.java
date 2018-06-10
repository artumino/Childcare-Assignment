package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.shared.entities.Gruppo;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;

public class GruppoRequestHandlerSet extends GenericDaoSetEntityRequestHandler<SetEntityRequest<Gruppo>, Gruppo>
{
    @Override
    protected Class<Gruppo> getQueryClass() {
        return Gruppo.class;
    }
}
