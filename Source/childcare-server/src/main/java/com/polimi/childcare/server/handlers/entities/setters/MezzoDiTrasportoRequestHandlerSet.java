package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.shared.entities.Fornitore;
import com.polimi.childcare.shared.entities.MezzoDiTrasporto;
import com.polimi.childcare.shared.entities.PianoViaggi;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;

import java.util.Set;

public class MezzoDiTrasportoRequestHandlerSet extends GenericSetEntityRequestHandler<SetEntityRequest<MezzoDiTrasporto>, MezzoDiTrasporto>
{
    @Override
    protected Class<MezzoDiTrasporto> getQueryClass() {
        return MezzoDiTrasporto.class;
    }

    @Override
    protected void doPreSetChecks(DatabaseSession.DatabaseSessionInstance session, SetEntityRequest<MezzoDiTrasporto> request, MezzoDiTrasporto dbEntity)
    {
        //FIXME: Fixed?
        if(!request.isToDelete())
        {
            if(request.getEntity().getFornitore() == null || request.getEntity().getTarga() == null)
                throw new RuntimeException("Fornitore e/o Targa Nullo!");

            DBHelper.updateManyToOne(request.getEntity().asMezziDiTrasportoFornitoreRelation(), Fornitore.class, session);
        }

        else
        {
            Set<PianoViaggi> pv = dbEntity.getPianoViaggi();

            for (PianoViaggi p : pv)
            {
                p.setMezzo(null);
                session.update(p);
            }
        }
    }
}
