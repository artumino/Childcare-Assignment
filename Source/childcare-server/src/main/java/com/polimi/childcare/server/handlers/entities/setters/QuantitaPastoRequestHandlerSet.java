package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.shared.entities.Menu;
import com.polimi.childcare.shared.entities.Pasto;
import com.polimi.childcare.shared.entities.QuantitaPasto;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;

public class QuantitaPastoRequestHandlerSet extends GenericSetEntityRequestHandler<SetEntityRequest<QuantitaPasto>, QuantitaPasto>
{
    @Override
    protected Class<QuantitaPasto> getQueryClass() {
        return QuantitaPasto.class;
    }

    @Override
    protected void doPreSetChecks(DatabaseSession.DatabaseSessionInstance session, SetEntityRequest<QuantitaPasto> request, QuantitaPasto dbEntity)
    {
        if (dbEntity != null && request.getOldHashCode() == dbEntity.consistecyHashCode())
        {
            if (!request.isToDelete())
            {
                if(request.getEntity().getMenu() == null || request.getEntity().getPasto() == null)
                    throw new RuntimeException("Gita e/o Gruppo nulli!");

                DBHelper.updateManyToOne(request.getEntity().asQuantitaPastiMenuRelation(), Menu.class, session);
                DBHelper.updateManyToOne(request.getEntity().asQuantitaPastiPastoRelation(), Pasto.class, session);
            }
        }
    }
}