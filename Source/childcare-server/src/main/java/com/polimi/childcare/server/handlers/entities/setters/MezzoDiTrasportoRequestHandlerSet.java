package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.shared.entities.MezzoDiTrasporto;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;

public class MezzoDiTrasportoRequestHandlerSet extends GenericDaoSetEntityRequestHandler<SetEntityRequest<MezzoDiTrasporto>, MezzoDiTrasporto>
{
    @Override
    protected Class<MezzoDiTrasporto> getQueryClass() {
        return MezzoDiTrasporto.class;
    }
}
