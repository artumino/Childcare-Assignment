package com.polimi.childcare.server.database.dao.implementations;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.database.dao.HibernateDao;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.entities.Gita;
import com.polimi.childcare.shared.entities.RegistroPresenze;

public class RegistroPresenzeDaoImpl extends HibernateDao<RegistroPresenze>
{
    public RegistroPresenzeDaoImpl(DatabaseSession.DatabaseSessionInstance sessionInstance) { super(sessionInstance); }

    @Override
    public void delete(RegistroPresenze item)
    {
        sessionInstance.delete(item);
    }

    @Override
    public int insert(RegistroPresenze item)
    {
        checkConstraints(item);
        int ID = sessionInstance.insert(item);
        DBHelper.updateManyToOne(item.asRegistroPresenzeBambinoRelation(), Bambino.class, sessionInstance);
        DBHelper.updateManyToOne(item.asRegistroPresenzeGitaRelation(), Gita.class, sessionInstance);
        return ID;
    }

    @Override
    public void update(RegistroPresenze item)
    {
        checkConstraints(item);
        RegistroPresenze dbEntity = sessionInstance.getByID(RegistroPresenze.class, item.getID());

        if(dbEntity != null)
        {
            DBHelper.updateManyToOne(item.asRegistroPresenzeBambinoRelation(), Bambino.class, sessionInstance);
            DBHelper.updateManyToOne(item.asRegistroPresenzeGitaRelation(), Gita.class, sessionInstance);
            sessionInstance.insertOrUpdate(item);
        }
        else
            insert(item);
    }

    private void checkConstraints(RegistroPresenze gruppo)
    {
        if (gruppo.getDate() == null || gruppo.getTimeStamp() == null || gruppo.getStato() == null)
            throw new RuntimeException("Campi obbligatori vuoti!");
    }
}
