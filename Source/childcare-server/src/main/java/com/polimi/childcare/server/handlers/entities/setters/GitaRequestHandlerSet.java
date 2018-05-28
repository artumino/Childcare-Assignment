package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.shared.entities.Gita;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

public class GitaRequestHandlerSet extends GenericSetEntityRequestHandler<SetEntityRequest<Gita>, Gita>
{
    @Override
    protected Class<Gita> getQueryClass() {
        return Gita.class;
    }

    @Override
    protected void doPreSetChecks(DatabaseSession.DatabaseSessionInstance session, SetEntityRequest<Gita> request, Gita dbEntity) {
        //TODO: Da fare
    }
}
