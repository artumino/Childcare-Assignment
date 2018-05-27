package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.shared.entities.Pasto;
import com.polimi.childcare.shared.entities.ReazioneAvversa;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import java.util.Set;

public class PastiRequestHandlerSet extends GenericSetEntityRequestHandler<SetEntityRequest<Pasto>, Pasto>
    @Override
    public BaseResponse processRequest(SetEntityRequest<Pasto> request)
    {
        //FIXME: Fix di questi
        final BaseResponse[] response = new BaseResponse[1];
        Throwable exception = DatabaseSession.getInstance().execute(session -> {
            Pasto pastoget = session.getByID(Pasto.class, request.getEntity().getID(), true);
            Set<ReazioneAvversa> rv = pastoget.getReazione();

            if(request.isToDelete())
            {
                for (ReazioneAvversa r : rv) {
                    pastoget.removeReazione(r);
                }

                session.update(pastoget);
            }

            return !((response[0] = requestSet(request, Pasto.class, session)) instanceof BadRequestResponse);
        });

        if(exception != null)
            return new BadRequestResponse.BadRequestResponseWithMessage(exception.getMessage());
        return response[0];
    }
}