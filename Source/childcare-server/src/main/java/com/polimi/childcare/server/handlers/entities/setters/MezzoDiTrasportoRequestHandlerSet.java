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
    protected Class<MezzoDiTrasporto> getQueryClass() {
        return MezzoDiTrasporto.class;
    }

    @Override
    protected void doPreSetChecks(DatabaseSession.DatabaseSessionInstance session, SetEntityRequest<MezzoDiTrasporto> request, MezzoDiTrasporto dbEntity) {
        //FIXME: Fixarlo
        Set<PianoViaggi> pv = dbEntity.getPianoViaggi();

        if(request.isToDelete())
        {
            for (PianoViaggi p : pv)
            {
                p.setMezzo(null);
                session.update(p);
            }
        }
    }
}
