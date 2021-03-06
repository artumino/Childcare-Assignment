package com.polimi.childcare.server.database.dao.implementations;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.database.dao.HibernateDao;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.entities.Contatto;

import java.util.Set;

public class ContattoDaoImpl extends HibernateDao<Contatto>
{
    public ContattoDaoImpl(DatabaseSession.DatabaseSessionInstance sessionInstance) { super(sessionInstance); }

    @Override
    public void delete(Contatto item)
    {
        Contatto dbEntity = sessionInstance.getByID(Contatto.class, item.getID());
        Set<Bambino> bambiniset = dbEntity.getBambini();

        for (Bambino b : bambiniset)
            dbEntity.removeBambino(b);

        sessionInstance.delete(item);
    }

    @Override
    public int insert(Contatto item)
    {
        checkConstraints(item);
        int ID = sessionInstance.insert(item);
        DBHelper.updateManyToManyOwner(item.asContattiBambiniRelation(), Bambino.class, sessionInstance);
        return ID;
    }

    @Override
    public void update(Contatto item)
    {
        checkConstraints(item);
        Contatto dbEntity = sessionInstance.getByID(Contatto.class, item.getID());

        if(dbEntity != null)
        {
            DBHelper.updateManyToManyOwner(item.asContattiBambiniRelation(), Bambino.class, sessionInstance);
            sessionInstance.insertOrUpdate(item);
        }
        else
            insert(item);

    }

    private void checkConstraints(Contatto contatto)
    {
        if(contatto.getNome() == null ||
                contatto.getCognome() == null ||
                contatto.getIndirizzo() == null)
            throw new RuntimeException("Un campo obbligatorio è null!");
    }
}
