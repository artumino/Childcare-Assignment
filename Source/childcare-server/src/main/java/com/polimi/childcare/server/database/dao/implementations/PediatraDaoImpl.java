package com.polimi.childcare.server.database.dao.implementations;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.database.dao.HibernateDao;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.entities.Pediatra;

import java.util.Set;

public class PediatraDaoImpl extends HibernateDao<Pediatra>
{
    public PediatraDaoImpl(DatabaseSession.DatabaseSessionInstance sessionInstance) { super(sessionInstance); }

    @Override
    public void delete(Pediatra gruppo)
    {
        Pediatra dbEntity = sessionInstance.getByID(Pediatra.class, gruppo.getID());
        Set<Bambino> bambiniset = dbEntity.getBambini();

        for (Bambino b : bambiniset)
            dbEntity.removeBambino(b);

        sessionInstance.delete(gruppo);
    }

    @Override
    public int insert(Pediatra gruppo)
    {
        checkConstraints(gruppo);
        int ID = sessionInstance.insert(gruppo);
        DBHelper.updateManyToManyOwner(gruppo.asContattiBambiniRelation(), Bambino.class, sessionInstance);
        return ID;
    }

    @Override
    public void update(Pediatra gruppo)
    {
        checkConstraints(gruppo);
        Pediatra dbEntity = sessionInstance.getByID(Pediatra.class, gruppo.getID());

        if(dbEntity != null)
        {
            DBHelper.updateManyToManyOwner(gruppo.asContattiBambiniRelation(), Bambino.class, sessionInstance);
            sessionInstance.insertOrUpdate(gruppo);
        }
        else
            insert(gruppo);
    }

    private void checkConstraints(Pediatra gruppo)
    {
        if(gruppo.getNome() == null ||
                gruppo.getCognome() == null ||
                gruppo.getIndirizzo() == null)
            throw new RuntimeException("Un campo obbligatorio è null!");
    }
}
