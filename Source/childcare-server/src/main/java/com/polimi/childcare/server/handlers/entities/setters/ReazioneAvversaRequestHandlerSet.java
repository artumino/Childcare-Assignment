package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.shared.entities.ReazioneAvversa;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;

public class ReazioneAvversaRequestHandlerSet extends GenericDaoSetEntityRequestHandler<SetEntityRequest<ReazioneAvversa>, ReazioneAvversa>
{

    @Override
    protected Class<ReazioneAvversa> getQueryClass() {
        return ReazioneAvversa.class;
    }
}