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
    public void delete(Menu item)
    {
        Menu dbEntity = sessionInstance.getByID(Menu.class, item.getID());
        Set<Pasto> pastoset = dbEntity.getPasti();

        for (Pasto p : pastoset)
            dbEntity.removePasto(p);

        sessionInstance.delete(item);
    }

    @Override
    public int insert(Menu item)
    {
        checkConstraints(item);
        int ID = sessionInstance.insert(item);
        DBHelper.updateManyToManyOwner(item.asMenuPastoRelation(), Pasto.class, sessionInstance);
        return ID;
    }

    @Override
    public void update(Menu item)
    {
        checkConstraints(item);
        Menu dbEntity = sessionInstance.getByID(Menu.class, item.getID());

        if(dbEntity != null)
        {
            DBHelper.updateManyToManyOwner(item.asMenuPastoRelation(), Pasto.class, sessionInstance);
            sessionInstance.insertOrUpdate(item);
        }
        else
            sessionInstance.insert(item);
    }

    private void checkConstraints(Menu menu)
    {
        if (menu.getNome() == null)
            throw new RuntimeException("Nome è null!");
    }
}
