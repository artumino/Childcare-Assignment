package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.shared.entities.QuantitaPasto;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

public class QuantitaPastoRequestHandlerSet extends GenericSetEntityRequestHandler<SetEntityRequest<QuantitaPasto>, QuantitaPasto>
{
    @Override
    protected Class<QuantitaPasto> getQueryClass() {
        return QuantitaPasto.class;
    }

    @Override
    protected void doPreSetChecks(DatabaseSession.DatabaseSessionInstance session, SetEntityRequest<QuantitaPasto> request, QuantitaPasto dbEntity) {
        //TODO: Da fare
    }
}