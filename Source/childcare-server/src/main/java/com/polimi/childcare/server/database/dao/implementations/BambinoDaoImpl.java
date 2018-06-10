package com.polimi.childcare.server.database.dao.implementations;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.database.dao.HibernateDao;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.shared.entities.*;

public class BambinoDaoImpl extends HibernateDao<Bambino>
{
    public BambinoDaoImpl(DatabaseSession.DatabaseSessionInstance sessionInstance) { super(sessionInstance); }

    @Override
    public void delete(Bambino gruppo)
    {
        Bambino dbEntity = sessionInstance.getByID(Bambino.class, gruppo.getID());
        DBHelper.deletedManyToManyOwned(gruppo.asBambiniContattiRelation(), dbEntity.asBambiniContattiRelation(), Contatto.class, sessionInstance);
        sessionInstance.delete(gruppo);
    }

    @Override
    public int insert(Bambino gruppo)
    {
        checkConstraints(gruppo);
        int ID = sessionInstance.insert(gruppo);
        DBHelper.updateManyToOne(gruppo.asBambiniPediatraRelation(), Pediatra.class, sessionInstance);
        DBHelper.updateManyToOne(gruppo.asBambiniGruppoRelation(), Gruppo.class, sessionInstance);
        DBHelper.updateManyToManyOwner(gruppo.asBambiniGenitoriRelation(), Genitore.class, sessionInstance);
        DBHelper.updateManyToManyOwned(gruppo.asBambiniContattiRelation(), gruppo.asBambiniContattiRelation(), Contatto.class, sessionInstance);
        return ID;
    }

    @Override
    public void update(Bambino gruppo)
    {
        checkConstraints(gruppo);
        Bambino dbEntity = sessionInstance.getByID(Bambino.class, gruppo.getID());

        if(dbEntity != null)
        {
            DBHelper.updateManyToOne(gruppo.asBambiniPediatraRelation(), Pediatra.class, sessionInstance);
            DBHelper.updateManyToOne(gruppo.asBambiniGruppoRelation(), Gruppo.class, sessionInstance);
            DBHelper.updateManyToManyOwner(gruppo.asBambiniGenitoriRelation(), Genitore.class, sessionInstance);
            DBHelper.updateManyToManyOwned(gruppo.asBambiniContattiRelation(), dbEntity.asBambiniContattiRelation(), Contatto.class, sessionInstance);
            sessionInstance.insertOrUpdate(gruppo);
        }
        else
            insert(gruppo);

    }

    private void checkConstraints(Bambino gruppo)
    {
        if (gruppo.getNome() == null ||
                gruppo.getCognome() == null ||
                gruppo.getCodiceFiscale() == null ||
                gruppo.getDataNascita() == null ||
                gruppo.getStato() == null ||
                gruppo.getComune() == null ||
                gruppo.getProvincia() == null ||
                gruppo.getCittadinanza() == null ||
                gruppo.getResidenza() == null ||
                gruppo.getSesso() == null)
            throw new RuntimeException("Un campo obbligatorio è null!");
    }
}
