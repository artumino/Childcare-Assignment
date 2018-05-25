package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.Pasto;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

public class SetPasti implements IRequestHandler<SetEntityRequest<Pasto>>
{
    @Override
    public BaseResponse processRequest(SetEntityRequest<Pasto> request)
    {
        return SetGenericEntity.Setter(request, Pasto.class);
    }
}