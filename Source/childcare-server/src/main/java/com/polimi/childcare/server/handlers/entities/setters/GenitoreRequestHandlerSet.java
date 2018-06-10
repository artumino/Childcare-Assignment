package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.shared.entities.Genitore;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;

public class GenitoreRequestHandlerSet extends GenericDaoSetEntityRequestHandler<SetEntityRequest<Genitore>, Genitore>
{
    @Override
    protected Class<Genitore> getQueryClass() {
        return Genitore.class;
    }
}
