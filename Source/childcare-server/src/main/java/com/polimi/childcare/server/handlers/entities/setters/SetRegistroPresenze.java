package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.RegistroPresenze;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

public class SetRegistroPresenze implements IRequestHandler<SetEntityRequest<RegistroPresenze>>
{
    @Override
    public BaseResponse processRequest(SetEntityRequest<RegistroPresenze> request)
    {
        return SetGenericEntity.Setter(request, RegistroPresenze.class);
    }
}