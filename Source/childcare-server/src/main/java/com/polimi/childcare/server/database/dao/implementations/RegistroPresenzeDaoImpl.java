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
    public void delete(RegistroPresenze gruppo)
    {
        sessionInstance.delete(gruppo);
    }

    @Override
    public int insert(RegistroPresenze gruppo)
    {
        checkConstraints(gruppo);
        int ID = sessionInstance.insert(gruppo);
        DBHelper.updateManyToOne(gruppo.asRegistroPresenzeBambinoRelation(), Bambino.class, sessionInstance);
        DBHelper.updateManyToOne(gruppo.asRegistroPresenzeGitaRelation(), Gita.class, sessionInstance);
        return ID;
    }

    @Override
    public void update(RegistroPresenze gruppo)
    {
        checkConstraints(gruppo);
        RegistroPresenze dbEntity = sessionInstance.getByID(RegistroPresenze.class, gruppo.getID());

        if(dbEntity != null)
        {
            DBHelper.updateManyToOne(gruppo.asRegistroPresenzeBambinoRelation(), Bambino.class, sessionInstance);
            DBHelper.updateManyToOne(gruppo.asRegistroPresenzeGitaRelation(), Gita.class, sessionInstance);
            sessionInstance.insertOrUpdate(gruppo);
        }
        else
            insert(gruppo);
    }

    private void checkConstraints(RegistroPresenze gruppo)
    {
        if (gruppo.getDate() == null || gruppo.getTimeStamp() == null || gruppo.getStato() == null)
            throw new RuntimeException("Campi obbligatori vuoti!");
    }
}
