package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.shared.entities.MezzoDiTrasporto;
import com.polimi.childcare.shared.entities.PianoViaggi;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

import java.util.Set;

public class MezzoDiTrasportoRequestHandlerSet extends GenericSetEntityRequestHandler<SetEntityRequest<MezzoDiTrasporto>, MezzoDiTrasporto>
{
    @Override
    public BaseResponse processRequest(SetEntityRequest<MezzoDiTrasporto> request)
    {
        //FIXME: Fixarlo
        final BaseResponse[] response = new BaseResponse[1];
        Throwable exception = DatabaseSession.getInstance().execute(session ->
        {
            MezzoDiTrasporto mezzoget = session.getByID(MezzoDiTrasporto.class, request.getEntity().getID(), true);
            Set<PianoViaggi> pv = mezzoget.getPianoViaggi();

            if(request.isToDelete())
            {
                for (PianoViaggi p : pv)
                {
                    p.setMezzo(null);
                    session.update(p);
                }
            }
            return !((response[0] = requestSet(request, MezzoDiTrasporto.class, session)) instanceof BadRequestResponse);
        });

        if(exception != null)
            return new BadRequestResponse.BadRequestResponseWithMessage(exception.getMessage());

        return response[0];
    }
}
