package com.polimi.childcare.server.database.dao.implementations;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.database.dao.HibernateDao;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.shared.entities.Menu;
import com.polimi.childcare.shared.entities.Pasto;

import java.util.Set;

public class MenuDaoImpl extends HibernateDao<Menu>
{
    public MenuDaoImpl(DatabaseSession.DatabaseSessionInstance sessionInstance) { super(sessionInstance); }

    @Override
    public void delete(Menu gruppo)
    {
        Menu dbEntity = sessionInstance.getByID(Menu.class, gruppo.getID());
        Set<Pasto> pastoset = dbEntity.getPasti();

        for (Pasto p : pastoset)
            dbEntity.removePasto(p);

        sessionInstance.delete(gruppo);
    }

    @Override
    public int insert(Menu gruppo)
    {
        checkConstraints(gruppo);
        int ID = sessionInstance.insert(gruppo);
        DBHelper.updateManyToManyOwner(gruppo.asMenuPastoRelation(), Pasto.class, sessionInstance);
        return ID;
    }

    @Override
    public void update(Menu gruppo)
    {
        checkConstraints(gruppo);
        Menu dbEntity = sessionInstance.getByID(Menu.class, gruppo.getID());

        if(dbEntity != null)
        {
            DBHelper.updateManyToManyOwner(gruppo.asMenuPastoRelation(), Pasto.class, sessionInstance);
            sessionInstance.insertOrUpdate(gruppo);
        }
        else
            sessionInstance.insert(gruppo);
    }

    private void checkConstraints(Menu gruppo)
    {
        if (gruppo.getNome() == null)
            throw new RuntimeException("Nome è null!");
    }
}
