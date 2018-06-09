package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.shared.entities.Fornitore;
import com.polimi.childcare.shared.entities.Menu;
import com.polimi.childcare.shared.entities.Pasto;
import com.polimi.childcare.shared.entities.ReazioneAvversa;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;

import java.util.Set;

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
                DBHelper.updateManyToManyOwned(request.getEntity().asPastoMenuRelation(), dbEntity.asPastoMenuRelation(), Menu.class, session);
                DBHelper.updateManyToManyOwner(request.getEntity().asPastoReazioniAvverseRelation(), ReazioneAvversa.class, session);
            }
            else {
                DBHelper.deletedManyToManyOwned(request.getEntity().asPastoMenuRelation(), dbEntity.asPastoMenuRelation(), Menu.class, session);

                Set<ReazioneAvversa> reazioni = dbEntity.getReazione();

                for (ReazioneAvversa r : reazioni)
                    dbEntity.removeReazione(r);
            }
        }
    }
}