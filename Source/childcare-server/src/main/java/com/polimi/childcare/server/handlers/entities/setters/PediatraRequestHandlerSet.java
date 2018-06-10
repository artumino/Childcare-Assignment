package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.shared.entities.Pediatra;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;

public class PediatraRequestHandlerSet extends GenericDaoSetEntityRequestHandler<SetEntityRequest<Pediatra>, Pediatra>
{
    @Override
    protected Class<Pediatra> getQueryClass() {
        return Pediatra.class;
    }
}