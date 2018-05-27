package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.Pasto;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

public class SetPasti implements IRequestHandler<SetEntityRequest<Pasto>>
{
    @Override
    public BaseResponse processRequest(SetEntityRequest<Pasto> request)
    {
        final BaseResponse[] response = new BaseResponse[1];
        DatabaseSession.getInstance().execute(session -> {
            return !((response[0] = SetGenericEntity.Setter(request, Pasto.class, session)) instanceof BadRequestResponse);
        });
        return response[0];
    }
}