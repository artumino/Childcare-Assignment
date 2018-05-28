package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.shared.entities.Pasto;
import com.polimi.childcare.shared.entities.ReazioneAvversa;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

import java.util.Set;

public class ReazioneAvversaRequestHandlerSet extends GenericSetEntityRequestHandler<SetEntityRequest<ReazioneAvversa>, ReazioneAvversa>
{

    @Override
    protected Class<ReazioneAvversa> getQueryClass() {
        return ReazioneAvversa.class;
    }

    @Override
    protected void doPreSetChecks(DatabaseSession.DatabaseSessionInstance session, SetEntityRequest<ReazioneAvversa> request, ReazioneAvversa dbEntity) {
        //FIXME: Da sistemare
        Set<Pasto> pastoset = dbEntity.getPasti();

        if(request.isToDelete())
        {
            for (Pasto p : pastoset)
            {
                p.removeReazione(dbEntity);
                session.update(p);
            }
        }
    }
}