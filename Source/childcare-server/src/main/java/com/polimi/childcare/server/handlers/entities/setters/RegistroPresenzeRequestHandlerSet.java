package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.shared.entities.RegistroPresenze;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

public class RegistroPresenzeRequestHandlerSet extends GenericSetEntityRequestHandler<SetEntityRequest<RegistroPresenze>, RegistroPresenze>
{
    @Override
    protected Class<RegistroPresenze> getQueryClass() {
        return RegistroPresenze.class;
    }

    @Override
    protected void doPreSetChecks(DatabaseSession.DatabaseSessionInstance session, SetEntityRequest<RegistroPresenze> request, RegistroPresenze dbEntity) {
        //TODO: Da fare
    }
}