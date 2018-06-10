package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.shared.entities.RegistroPresenze;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;

public class RegistroPresenzeRequestHandlerSet extends GenericDaoSetEntityRequestHandler<SetEntityRequest<RegistroPresenze>, RegistroPresenze>
{
    @Override
    protected Class<RegistroPresenze> getQueryClass() {
        return RegistroPresenze.class;
    }
}