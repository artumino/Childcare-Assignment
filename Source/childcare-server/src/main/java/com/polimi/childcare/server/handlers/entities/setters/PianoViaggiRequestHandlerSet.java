package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.shared.entities.PianoViaggi;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

public class PianoViaggiRequestHandlerSet extends GenericSetEntityRequestHandler<SetEntityRequest<PianoViaggi>, PianoViaggi>
{
    @Override
    protected Class<PianoViaggi> getQueryClass() {
        return PianoViaggi.class;
    }

    @Override
    protected void doPreSetChecks(DatabaseSession.DatabaseSessionInstance session, SetEntityRequest<PianoViaggi> request, PianoViaggi dbEntity) {
        //TODO: Da fare
    }
}