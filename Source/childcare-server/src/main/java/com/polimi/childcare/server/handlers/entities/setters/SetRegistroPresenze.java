package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.RegistroPresenze;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

public class SetRegistroPresenze implements IRequestHandler<SetEntityRequest<RegistroPresenze>>
{
    @Override
    public BaseResponse processRequest(SetEntityRequest<RegistroPresenze> request)
    {
        final BaseResponse[] response = new BaseResponse[1];
        Throwable exception = DatabaseSession.getInstance().execute(session -> {
            return !((response[0] = SetGenericEntity.Setter(request, RegistroPresenze.class, session)) instanceof BadRequestResponse);
        });

        if(exception != null)
            return new BadRequestResponse.BadRequestResponseWithMessage(exception.getMessage());

        return response[0];
    }
}