package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.MezzoDiTrasporto;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

public class SetMezzoDiTrasporto implements IRequestHandler<SetEntityRequest<MezzoDiTrasporto>>
{
    @Override
    public BaseResponse processRequest(SetEntityRequest<MezzoDiTrasporto> request)
    {
        return SetGenericEntity.Setter(request, MezzoDiTrasporto.class);
    }
}
