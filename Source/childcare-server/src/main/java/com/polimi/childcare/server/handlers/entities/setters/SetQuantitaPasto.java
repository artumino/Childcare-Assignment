package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.QuantitaPasto;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

public class SetQuantitaPasto implements IRequestHandler<SetEntityRequest<QuantitaPasto>>
{
    @Override
    public BaseResponse processRequest(SetEntityRequest<QuantitaPasto> request)
    {
        final BaseResponse[] response = new BaseResponse[1];
        Throwable exception = DatabaseSession.getInstance().execute(session -> {
            return !((response[0] = SetGenericEntity.Setter(request, QuantitaPasto.class, session)) instanceof BadRequestResponse);
        });

        if(exception != null)
            return new BadRequestResponse.BadRequestResponseWithMessage(exception.getMessage());

        return response[0];
    }
}