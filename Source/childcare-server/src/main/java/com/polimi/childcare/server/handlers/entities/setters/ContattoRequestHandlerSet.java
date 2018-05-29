package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.entities.Contatto;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

import java.util.Set;

public class ContattoRequestHandlerSet extends GenericSetEntityRequestHandler<SetEntityRequest<Contatto>, Contatto>
{
    @Override
    protected Class<Contatto> getQueryClass() {
        return Contatto.class;
    }

    @Override
    protected void doPreSetChecks(DatabaseSession.DatabaseSessionInstance session, SetEntityRequest<Contatto> request, Contatto dbEntity)
    {
        if (dbEntity != null && request.getOldHashCode() == dbEntity.consistecyHashCode())
        {
            if (!request.isToDelete())
                DBHelper.updateManyToManyOwner(request.getEntity().asContattiBambiniRelation(), Bambino.class, session);
            else
            {
                //TODO: ma qua va bene così?

                Set<Bambino> bambiniset = dbEntity.getBambini();

                for (Bambino b : bambiniset)
                    dbEntity.removeBambino(b);
            }

        }
    }
}