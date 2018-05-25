package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.Contatto;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

public class SetContatto implements IRequestHandler<SetEntityRequest<Contatto>>
{
    @Override
    public BaseResponse processRequest(SetEntityRequest<Contatto> request)
    {
        return SetGenericEntity.Setter(request, Contatto.class);
    }
}