package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.shared.entities.Gita;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;

public class GitaRequestHandlerSet extends GenericSetEntityRequestHandler<SetEntityRequest<Gita>, Gita>
{
    @Override
    protected Class<Gita> getQueryClass() {
        return Gita.class;
    }

    @Override
    protected void doPreSetChecks(DatabaseSession.DatabaseSessionInstance session, SetEntityRequest<Gita> request, Gita dbEntity)
    {
        if(!request.isToDelete())
        {
            if (request.getEntity().getDataInizio() == null ||
                    request.getEntity().getDataFine() == null ||
                    request.getEntity().getLuogo() == null)
                throw new RuntimeException("Un campo obbligatorio è null!");
        }
    }
}
