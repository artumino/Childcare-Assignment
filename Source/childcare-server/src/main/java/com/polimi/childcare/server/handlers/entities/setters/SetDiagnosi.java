package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.Diagnosi;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

public class SetDiagnosi implements IRequestHandler<SetEntityRequest<Diagnosi>>
{
    @Override
    public BaseResponse processRequest(SetEntityRequest<Diagnosi> request)
    {
        return SetGenericEntity.Setter(request, Diagnosi.class);
    }
}