package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.shared.entities.Pasto;
import com.polimi.childcare.shared.entities.ReazioneAvversa;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;

import java.util.Set;

public class ReazioneAvversaRequestHandlerSet extends GenericSetEntityRequestHandler<SetEntityRequest<ReazioneAvversa>, ReazioneAvversa>
{

    @Override
    protected Class<ReazioneAvversa> getQueryClass() {
        return ReazioneAvversa.class;
    }

    @Override
    protected void doPreSetChecks(DatabaseSession.DatabaseSessionInstance session, SetEntityRequest<ReazioneAvversa> request, ReazioneAvversa dbEntity)
    {
        if (dbEntity != null && request.getOldHashCode() == dbEntity.consistecyHashCode())
        {
            if (!request.isToDelete())
            {
                if(request.getEntity().getNome() == null)
                    throw new RuntimeException("Nome Null!");

                DBHelper.updateManyToManyOwned(request.getEntity().asReazioniAvversePastiRelation(), dbEntity.asReazioniAvversePastiRelation(), Pasto.class, session);
            }

            else
                DBHelper.deletedManyToManyOwned(request.getEntity().asReazioniAvversePastiRelation(), dbEntity.asReazioniAvversePastiRelation(), Pasto.class, session);
        }
    }
}