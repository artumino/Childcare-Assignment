package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.QuantitaPasto;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

public class SetQuantitaPasto implements IRequestHandler<SetEntityRequest<QuantitaPasto>>
{
    @Override
    public BaseResponse processRequest(SetEntityRequest<QuantitaPasto> request)
    {
        return SetGenericEntity.Setter(request, QuantitaPasto.class);
    }
}