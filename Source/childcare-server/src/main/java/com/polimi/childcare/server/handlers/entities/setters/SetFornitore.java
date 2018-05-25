package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.Fornitore;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

public class SetFornitore implements IRequestHandler<SetEntityRequest<Fornitore>>
{
    @Override
    public BaseResponse processRequest(SetEntityRequest<Fornitore> request)
    {
        return SetGenericEntity.Setter(request, Fornitore.class);
    }
}