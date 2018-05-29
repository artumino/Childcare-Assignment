package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.shared.entities.Diagnosi;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

public class DiagnosiRequestHandlerSet extends GenericSetEntityRequestHandler<SetEntityRequest<Diagnosi>, Diagnosi>
{
    @Override
    protected Class<Diagnosi> getQueryClass() {
        return Diagnosi.class;
    }

    @Override
    protected void doPreSetChecks(DatabaseSession.DatabaseSessionInstance session, SetEntityRequest<Diagnosi> request, Diagnosi dbEntity) { }
}