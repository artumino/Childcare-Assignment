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
    public void delete(Pediatra item)
    {
        Pediatra dbEntity = sessionInstance.getByID(Pediatra.class, item.getID());
        Set<Bambino> bambiniset = dbEntity.getBambini();

        for (Bambino b : bambiniset)
            dbEntity.removeBambino(b);

        sessionInstance.delete(item);
    }

    @Override
    public int insert(Pediatra item)
    {
        checkConstraints(item);
        int ID = sessionInstance.insert(item);
        DBHelper.updateManyToManyOwner(item.asContattiBambiniRelation(), Bambino.class, sessionInstance);
        return ID;
    }

    @Override
    public void update(Pediatra item)
    {
        checkConstraints(item);
        Pediatra dbEntity = sessionInstance.getByID(Pediatra.class, item.getID());

        if(dbEntity != null)
        {
            DBHelper.updateManyToManyOwner(item.asContattiBambiniRelation(), Bambino.class, sessionInstance);
            sessionInstance.insertOrUpdate(item);
        }
        else
            insert(item);
    }

    private void checkConstraints(Pediatra pediatra)
    {
        if(pediatra.getNome() == null ||
                pediatra.getCognome() == null ||
                pediatra.getIndirizzo() == null)
            throw new RuntimeException("Un campo obbligatorio è null!");
    }
}
