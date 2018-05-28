package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.shared.entities.Pasto;
import com.polimi.childcare.shared.entities.ReazioneAvversa;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import java.util.Set;

public class PastiRequestHandlerSet extends GenericSetEntityRequestHandler<SetEntityRequest<Pasto>, Pasto>
{

    @Override
    protected Class<Pasto> getQueryClass() {
        return Pasto.class;
    }

    @Override
    protected void doPreSetChecks(DatabaseSession.DatabaseSessionInstance session, SetEntityRequest<Pasto> request, Pasto dbEntity) {
        //FIXME: Fixarlo
        Set<ReazioneAvversa> rv = dbEntity.getReazione();

        if(request.isToDelete())
        {
            for (ReazioneAvversa r : rv) {
                dbEntity.removeReazione(r);
            }

            session.update(dbEntity);
        }
    }
}