package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.shared.entities.Menu;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;

public class MenuRequestHandlerSet extends GenericDaoSetEntityRequestHandler<SetEntityRequest<Menu>, Menu>
{
    @Override
    protected Class<Menu> getQueryClass() {
        return Menu.class;
    }
}