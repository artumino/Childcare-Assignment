package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.shared.entities.Gita;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;

public class GitaRequestHandlerSet extends GenericDaoSetEntityRequestHandler<SetEntityRequest<Gita>, Gita>
{
    @Override
    protected Class<Gita> getQueryClass() { return Gita.class; }
}
