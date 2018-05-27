package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.shared.entities.Pediatra;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

public class PediatraRequestHandlerSet extends GenericSetEntityRequestHandler<SetEntityRequest<Pediatra>, Pediatra>
{
    @Override
    public BaseResponse processRequest(SetEntityRequest<Pediatra> request)
    {
        final BaseResponse[] response = new BaseResponse[1];
        DatabaseSession.getInstance().execute(session -> {
            return !((response[0] = requestSet(request, Pediatra.class, session)) instanceof BadRequestResponse);
        });
        return response[0];
    }
}