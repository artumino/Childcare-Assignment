package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.entities.Gita;
import com.polimi.childcare.shared.entities.RegistroPresenze;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;

public class RegistroPresenzeRequestHandlerSet extends GenericSetEntityRequestHandler<SetEntityRequest<RegistroPresenze>, RegistroPresenze>
{
    @Override
    protected Class<RegistroPresenze> getQueryClass() {
        return RegistroPresenze.class;
    }

    @Override
    protected void doPreSetChecks(DatabaseSession.DatabaseSessionInstance session, SetEntityRequest<RegistroPresenze> request, RegistroPresenze dbEntity)
    {
        if (dbEntity != null && request.getOldHashCode() == dbEntity.consistecyHashCode())
        {
            if (!request.isToDelete())
            {
                if (request.getEntity().getDate() == null || request.getEntity().getTimeStamp() == null || request.getEntity().getStato() == null)
                    throw new RuntimeException("Campi obbligatori vuoti!");

                DBHelper.updateManyToOne(request.getEntity().asRegistroPresenzeBambinoRelation(), Bambino.class, session);
                DBHelper.updateManyToOne(request.getEntity().asRegistroPresenzeGitaRelation(), Gita.class, session);
            }
        }
    }
}