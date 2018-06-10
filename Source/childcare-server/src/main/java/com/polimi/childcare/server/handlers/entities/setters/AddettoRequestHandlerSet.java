package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.shared.entities.Addetto;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;

public class AddettoRequestHandlerSet extends GenericDaoSetEntityRequestHandler<SetEntityRequest<Addetto>, Addetto>
{
    @Override
    protected Class<Addetto> getQueryClass()
    {
        return Addetto.class;
    }
}
