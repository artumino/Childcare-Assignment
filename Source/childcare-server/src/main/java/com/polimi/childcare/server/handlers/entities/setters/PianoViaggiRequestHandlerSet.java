package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.shared.entities.PianoViaggi;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;

public class PianoViaggiRequestHandlerSet extends GenericDaoSetEntityRequestHandler<SetEntityRequest<PianoViaggi>, PianoViaggi>
{
    @Override
    protected Class<PianoViaggi> getQueryClass() {
        return PianoViaggi.class;
    }
}