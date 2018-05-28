package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.shared.entities.Addetto;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

public class AddettoRequestHandlerSet extends GenericSetEntityRequestHandler<SetEntityRequest<Addetto>, Addetto>
{
    @Override
    protected Class<Addetto> getQueryClass()
    {
        return Addetto.class;
    }

    @Override
    protected void doPreSetChecks(DatabaseSession.DatabaseSessionInstance session, SetEntityRequest<Addetto> request, Addetto dbEntity) {
        //TODO: Da Fare
    }
}
