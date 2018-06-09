package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.shared.entities.Fornitore;
import com.polimi.childcare.shared.entities.MezzoDiTrasporto;
import com.polimi.childcare.shared.entities.Pasto;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

import java.util.Set;

public class FornitoreRequestHandlerSet extends GenericSetEntityRequestHandler<SetEntityRequest<Fornitore>, Fornitore>
{
    @Override
    protected Class<Fornitore> getQueryClass() {
        return Fornitore.class;
    }

    @Override
    protected void doPreSetChecks(DatabaseSession.DatabaseSessionInstance session, SetEntityRequest<Fornitore> request, Fornitore dbEntity)
    {
        if(!request.isToDelete())
        {
            if (request.getEntity().getRagioneSociale() == null ||
                    request.getEntity().getPartitaIVA() == null ||
                    request.getEntity().getSedeLegale() == null ||
                    request.getEntity().getNumeroRegistroImprese() == null)
                throw new RuntimeException("Un campo obbligatorio è null!");


            //DBHelper.updateOneToMany(request.getEntity().asFornitorePastiRelation(), dbEntity.asFornitorePastiRelation(), Pasto.class, session);
            //DBHelper.updateOneToMany(request.getEntity().asFornitoreMezziDiTrasportoRelation(), dbEntity.asFornitoreMezziDiTrasportoRelation(), MezzoDiTrasporto.class, session);
        }

        /*if (request.isToDelete())
        {
            for(Pasto pasto : request.getEntity().getPasti())
                request.getEntity().unsafeRemovePasto(pasto);

            Set<Pasto> pianoViaggi = dbEntity.getPasti();
            for (Pasto pasto : pianoViaggi)
            {
                dbEntity.unsafeRemovePasto(pasto);
                pasto.setFornitore(null);
                session.delete(pasto);
            }
        }*/


    }
}