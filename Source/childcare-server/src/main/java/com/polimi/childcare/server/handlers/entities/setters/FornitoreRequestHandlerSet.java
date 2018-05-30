package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.shared.entities.Fornitore;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

public class FornitoreRequestHandlerSet extends GenericSetEntityRequestHandler<SetEntityRequest<Fornitore>, Fornitore>
{
    @Override
    protected Class<Fornitore> getQueryClass() {
        return Fornitore.class;
    }

    @Override
    protected void doPreSetChecks(DatabaseSession.DatabaseSessionInstance session, SetEntityRequest<Fornitore> request, Fornitore dbEntity)
    {
        /*TODO: Da fare nulla ha cascade :D e OneToMany*/
        if(!request.isToDelete())
        {
            if (request.getEntity().getRagioneSociale() == null ||
                    request.getEntity().getPartitaIVA() == null ||
                    request.getEntity().getSedeLegale() == null ||
                    request.getEntity().getNumeroRegistroImprese() == null)
                throw new RuntimeException("Un campo obbligatorio è null!");
        }

    }
}