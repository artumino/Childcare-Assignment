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
    public BaseResponse processRequest(SetEntityRequest<ReazioneAvversa> request)
    {
        final BaseResponse[] response = new BaseResponse[1];
        Throwable exception = DatabaseSession.getInstance().execute(session -> {
            ReazioneAvversa reazioneget = session.getByID(ReazioneAvversa.class, request.getEntity().getID(), true);
            Set<Pasto> pastoset = reazioneget.getPasti();

            if(request.isToDelete())
            {
                for (Pasto p : pastoset)
                {
                    p.removeReazione(reazioneget);
                    session.update(p);
                }
            }

            return !((response[0] = requestSet(request, ReazioneAvversa.class, session)) instanceof BadRequestResponse);
        });

        if(exception != null)
            return new BadRequestResponse.BadRequestResponseWithMessage(exception.getMessage());

        return response[0];
    }
}