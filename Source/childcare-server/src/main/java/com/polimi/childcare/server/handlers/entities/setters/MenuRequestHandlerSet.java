package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.shared.entities.Menu;
import com.polimi.childcare.shared.entities.Pasto;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;

import java.util.Set;

public class MenuRequestHandlerSet extends GenericSetEntityRequestHandler<SetEntityRequest<Menu>, Menu>
{
    @Override
    protected Class<Menu> getQueryClass() {
        return Menu.class;
    }

    @Override
    protected void doPreSetChecks(DatabaseSession.DatabaseSessionInstance session, SetEntityRequest<Menu> request, Menu dbEntity)
    {
        /*TODO: Ha le OneToMany :S e i Cascade*/

        if(!request.isToDelete())
        {
            if (request.getEntity().getNome() == null)
                throw new RuntimeException("Nome è null!");
        }
        else
        {
            Set<Pasto> pastoset = dbEntity.getPasti();

            for (Pasto p : pastoset)
                dbEntity.removePasto(p);
        }
    }
}