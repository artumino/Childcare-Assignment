package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.shared.entities.Diagnosi;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;

public class DiagnosiRequestHandlerSet extends GenericDaoSetEntityRequestHandler<SetEntityRequest<Diagnosi>, Diagnosi>
{
    @Override
    protected Class<Diagnosi> getQueryClass() {
        return Diagnosi.class;
    }
}