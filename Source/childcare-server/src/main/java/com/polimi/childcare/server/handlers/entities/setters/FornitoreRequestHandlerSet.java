package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.shared.entities.Fornitore;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;

public class FornitoreRequestHandlerSet extends GenericDaoSetEntityRequestHandler<SetEntityRequest<Fornitore>, Fornitore>
{
    @Override
    protected Class<Fornitore> getQueryClass() {
        return Fornitore.class;
    }
}