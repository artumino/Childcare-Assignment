package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.NumeroTelefono;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

public class SetNumeroDiTelefono implements IRequestHandler<SetEntityRequest<NumeroTelefono>>
{
    @Override
    public BaseResponse processRequest(SetEntityRequest<NumeroTelefono> request)
    {
        return SetGenericEntity.Setter(request, NumeroTelefono.class);
    }
}