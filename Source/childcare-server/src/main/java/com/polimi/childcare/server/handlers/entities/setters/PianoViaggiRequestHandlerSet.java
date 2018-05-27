package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.shared.entities.PianoViaggi;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

public class PianoViaggiRequestHandlerSet extends GenericSetEntityRequestHandler<SetEntityRequest<PianoViaggi>, PianoViaggi>
{
    @Override
    public BaseResponse processRequest(SetEntityRequest<PianoViaggi> request)
    {
        final BaseResponse[] response = new BaseResponse[1];
        DatabaseSession.getInstance().execute(session -> {
            return !((response[0] = requestSet(request, PianoViaggi.class, session)) instanceof BadRequestResponse);
        });
        return response[0];
    }
}