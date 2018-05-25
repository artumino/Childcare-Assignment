package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.Pediatra;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

public class SetPediatra implements IRequestHandler<SetEntityRequest<Pediatra>>
{
    @Override
    public BaseResponse processRequest(SetEntityRequest<Pediatra> request)
    {
        return SetGenericEntity.Setter(request, Pediatra.class);
    }
}