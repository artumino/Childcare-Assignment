package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

public class SetBambino implements IRequestHandler<SetEntityRequest<Bambino>>
{
    @Override
    public BaseResponse processRequest(SetEntityRequest<Bambino> request)
    {
        return SetGenericEntity.Setter(request, Bambino.class);
    }
}