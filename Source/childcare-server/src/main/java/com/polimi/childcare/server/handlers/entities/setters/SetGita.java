package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.Gita;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

public class SetGita implements IRequestHandler<SetEntityRequest<Gita>>
{
    @Override
    public BaseResponse processRequest(SetEntityRequest<Gita> request)
    {
        return SetGenericEntity.Setter(request, Gita.class);
    }
}
