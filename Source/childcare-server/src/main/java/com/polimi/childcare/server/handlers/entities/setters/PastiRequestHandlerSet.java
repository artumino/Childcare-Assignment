package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.shared.entities.Fornitore;
import com.polimi.childcare.shared.entities.Menu;
import com.polimi.childcare.shared.entities.Pasto;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;

public class PastiRequestHandlerSet extends GenericSetEntityRequestHandler<SetEntityRequest<Pasto>, Pasto>
{

    @Override
    protected Class<Pasto> getQueryClass() {
        return Pasto.class;
    }

    @Override
    protected void doPreSetChecks(DatabaseSession.DatabaseSessionInstance session, SetEntityRequest<Pasto> request, Pasto dbEntity)
    {
        if (dbEntity != null && request.getOldHashCode() == dbEntity.consistecyHashCode())
        {
            if (!request.isToDelete())
            {
                if(request.getEntity().getFornitore() == null || request.getEntity().getNome() == null)
                    throw new RuntimeException("Fornitore e/o Nome Nullo!");

                DBHelper.updateManyToOne(request.getEntity().asPastoFornitoreRelation(), Fornitore.class, session);
            }
            else
                DBHelper.deletedManyToManyOwned(request.getEntity().asPastoMenuRelation(), dbEntity.asPastoMenuRelation(), Menu.class, session);
                //DBHelper.deletedManyToManyOwned(request.getEntity().asPastoReazioniAvverseRelation(), dbEntity.asPastoReazioniAvverseRelation(), Pasto.class, session);
        }
    }
}