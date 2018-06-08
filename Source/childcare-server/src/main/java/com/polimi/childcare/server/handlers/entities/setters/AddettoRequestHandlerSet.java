package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.shared.entities.Addetto;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;

public class AddettoRequestHandlerSet extends GenericSetEntityRequestHandler<SetEntityRequest<Addetto>, Addetto>
{
    @Override
    protected Class<Addetto> getQueryClass()
    {
        return Addetto.class;
    }

    @Override
    protected void doPreSetChecks(DatabaseSession.DatabaseSessionInstance session, SetEntityRequest<Addetto> request, Addetto dbEntity)
    {
        if(!request.isToDelete())
        {
            if (request.getEntity().getNome() == null ||
                    request.getEntity().getCognome() == null ||
                    request.getEntity().getCodiceFiscale() == null ||
                    request.getEntity().getDataNascita() == null ||
                    request.getEntity().getStato() == null ||
                    request.getEntity().getComune() == null ||
                    request.getEntity().getProvincia() == null ||
                    request.getEntity().getCittadinanza() == null ||
                    request.getEntity().getResidenza() == null ||
                    request.getEntity().getSesso() == null)
                throw new RuntimeException("Un campo obbligatorio è null!");
        }
    }
}
