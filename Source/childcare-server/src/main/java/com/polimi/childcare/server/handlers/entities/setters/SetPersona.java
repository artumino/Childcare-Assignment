package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.Persona;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

public class SetPersona implements IRequestHandler<SetEntityRequest<Persona>>
{
    @Override
    public BaseResponse processRequest(SetEntityRequest<Persona> request)
    {
        return SetGenericEntity.Setter(request, Persona.class);
    }
}
