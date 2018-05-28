package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.entities.Gruppo;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

import java.util.Set;

public class GruppoRequestHandlerSet extends GenericSetEntityRequestHandler<SetEntityRequest<Gruppo>, Gruppo>
{
    @Override
    protected Class<Gruppo> getQueryClass() {
        return Gruppo.class;
    }

    @Override
    protected void doPreSetChecks(DatabaseSession.DatabaseSessionInstance session, SetEntityRequest<Gruppo> request, Gruppo dbEntity) {
        //FIXME: Da sistemare
        Set<Bambino> bambini = dbEntity.getBambini();

        if(request.isToDelete()) {
            for (Bambino b : bambini) {
                b.setGruppo(null);
                session.update(b);
            }
        }
    }
}
