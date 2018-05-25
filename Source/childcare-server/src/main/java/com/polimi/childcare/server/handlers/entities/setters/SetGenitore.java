package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.Genitore;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

public class SetGenitore implements IRequestHandler<SetEntityRequest<Genitore>>
{
    @Override
    public BaseResponse processRequest(SetEntityRequest<Genitore> request)
    {
        return SetGenericEntity.Setter(request, Genitore.class);
    }
}
