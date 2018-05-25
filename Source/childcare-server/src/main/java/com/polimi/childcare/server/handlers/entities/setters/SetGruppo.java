package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.Gruppo;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

public class SetGruppo implements IRequestHandler<SetEntityRequest<Gruppo>>
{
    @Override
    public BaseResponse processRequest(SetEntityRequest<Gruppo> request)
    {
        return SetGenericEntity.Setter(request, Gruppo.class);
    }
}
