package com.polimi.childcare.server.database.dao.implementations;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.database.dao.HibernateDao;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.shared.entities.*;

public class BambinoDaoImpl extends HibernateDao<Bambino>
{
    public BambinoDaoImpl(DatabaseSession.DatabaseSessionInstance sessionInstance) { super(sessionInstance); }

    @Override
    public void delete(Bambino item)
    {
        Bambino dbEntity = sessionInstance.getByID(Bambino.class, item.getID());
        DBHelper.deletedManyToManyOwned(item.asBambiniContattiRelation(), dbEntity.asBambiniContattiRelation(), Contatto.class, sessionInstance);
        sessionInstance.delete(item);
    }

    @Override
    public int insert(Bambino item)
    {
        checkConstraints(item);
        int ID = sessionInstance.insert(item);
        DBHelper.updateManyToOne(item.asBambiniPediatraRelation(), Pediatra.class, sessionInstance);
        DBHelper.updateManyToOne(item.asBambiniGruppoRelation(), Gruppo.class, sessionInstance);
        DBHelper.updateManyToManyOwner(item.asBambiniGenitoriRelation(), Genitore.class, sessionInstance);
        DBHelper.updateManyToManyOwned(item.asBambiniContattiRelation(), item.asBambiniContattiRelation(), Contatto.class, sessionInstance);
        //DBHelper.updateOneToMany(item.asPersonaDiagnosiRelation(), item.asPersonaDiagnosiRelation(), Diagnosi.class, sessionInstance);
        return ID;
    }

    @Override
    public void update(Bambino item)
    {
        checkConstraints(item);
        Bambino dbEntity = sessionInstance.getByID(Bambino.class, item.getID());

        if(dbEntity != null)
        {
            DBHelper.updateManyToOne(item.asBambiniPediatraRelation(), Pediatra.class, sessionInstance);
            DBHelper.updateManyToOne(item.asBambiniGruppoRelation(), Gruppo.class, sessionInstance);
            DBHelper.updateManyToManyOwner(item.asBambiniGenitoriRelation(), Genitore.class, sessionInstance);
            DBHelper.updateManyToManyOwned(item.asBambiniContattiRelation(), dbEntity.asBambiniContattiRelation(), Contatto.class, sessionInstance);
            //DBHelper.updateOneToMany(item.asPersonaDiagnosiRelation(), dbEntity.asPersonaDiagnosiRelation(), Diagnosi.class, sessionInstance);
            sessionInstance.insertOrUpdate(item);
        }
        else
            insert(item);

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
