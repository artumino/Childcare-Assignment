package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.shared.entities.Contatto;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;

public class ContattoRequestHandlerSet extends GenericDaoSetEntityRequestHandler<SetEntityRequest<Contatto>, Contatto>
{
    @Override
    protected Class<Contatto> getQueryClass() {
        return Contatto.class;
    }
}