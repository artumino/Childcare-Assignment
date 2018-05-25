package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.Menu;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

public class SetMenu implements IRequestHandler<SetEntityRequest<Menu>>
{
    @Override
    public BaseResponse processRequest(SetEntityRequest<Menu> request)
    {
        return SetGenericEntity.Setter(request, Menu.class);
    }
}