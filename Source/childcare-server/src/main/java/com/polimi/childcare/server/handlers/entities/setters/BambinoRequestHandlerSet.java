package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.shared.entities.*;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;

public class BambinoRequestHandlerSet extends GenericDaoSetEntityRequestHandler<SetEntityRequest<Bambino>, Bambino>
{
    @Override
    protected Class<Bambino> getQueryClass()
    {
        return Bambino.class;
    }
}