package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.PianoViaggi;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

public class SetPianoViaggi implements IRequestHandler<SetEntityRequest<PianoViaggi>>
{
    @Override
    public BaseResponse processRequest(SetEntityRequest<PianoViaggi> request)
    {
        return SetGenericEntity.Setter(request, PianoViaggi.class);
    }
}