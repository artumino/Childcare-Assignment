package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.entities.Gruppo;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;

import java.util.Set;

public class GruppoRequestHandlerSet extends GenericSetEntityRequestHandler<SetEntityRequest<Gruppo>, Gruppo>
{
    @Override
    protected Class<Gruppo> getQueryClass() {
        return Gruppo.class;
    }

    @Override
    protected void doPreSetChecks(DatabaseSession.DatabaseSessionInstance session, SetEntityRequest<Gruppo> request, Gruppo dbEntity)
    {
        if (dbEntity != null && request.getOldHashCode() == dbEntity.consistecyHashCode())
        {
            if(!request.isToDelete())
            {
                if (request.getEntity().getSorvergliante() == null)
                    throw new RuntimeException("Sorvegliante Nullo!");

                DBHelper.updateOneToMany(request.getEntity().asGruppoBambiniRelation(), dbEntity.asGruppoBambiniRelation(), Bambino.class, session);
            }
            if (request.isToDelete())
            {
                Set<Bambino> bambini = dbEntity.getBambini();

                if(request.isToDelete())
                {
                    for (Bambino b : bambini)
                    {
                        b.setGruppo(null);
                        session.update(b);
                    }
                }
            }
        }
    }

    @Override
    protected void doPostInsertOperations(DatabaseSession.DatabaseSessionInstance session, SetEntityRequest<Gruppo> request, Gruppo dbEntity) {
        super.doPostInsertOperations(session, request, dbEntity);

        DBHelper.updateOneToMany(request.getEntity().asGruppoBambiniRelation(), dbEntity.asGruppoBambiniRelation(), Bambino.class, session);
    }
}
