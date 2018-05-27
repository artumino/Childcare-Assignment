package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.shared.entities.ReazioneAvversa;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

public class ReazioneAvversaRequestHandlerSet extends GenericSetEntityRequestHandler<SetEntityRequest<ReazioneAvversa>, ReazioneAvversa>
{
    @Override
    public BaseResponse processRequest(SetEntityRequest<ReazioneAvversa> request)
    {
        final BaseResponse[] response = new BaseResponse[1];
        DatabaseSession.getInstance().execute(session -> {
            return !((response[0] = requestSet(request, ReazioneAvversa.class, session)) instanceof BadRequestResponse);
        });
        return response[0];
    }
}