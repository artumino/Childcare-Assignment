package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.shared.entities.MezzoDiTrasporto;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

public class MezzoDiTrasportoRequestHandlerSet extends GenericSetEntityRequestHandler<SetEntityRequest<MezzoDiTrasporto>, MezzoDiTrasporto>
{
    @Override
    public BaseResponse processRequest(SetEntityRequest<MezzoDiTrasporto> request)
    {
        final BaseResponse[] response = new BaseResponse[1];
        DatabaseSession.getInstance().execute(session -> {
            return !((response[0] = requestSet(request, MezzoDiTrasporto.class, session)) instanceof BadRequestResponse);
        });
        return response[0];
    }
}
