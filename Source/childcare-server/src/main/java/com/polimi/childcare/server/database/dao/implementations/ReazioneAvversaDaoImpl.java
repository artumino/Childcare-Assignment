package com.polimi.childcare.server.database.dao.implementations;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.database.dao.HibernateDao;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.shared.entities.Diagnosi;
import com.polimi.childcare.shared.entities.Pasto;
import com.polimi.childcare.shared.entities.ReazioneAvversa;

public class ReazioneAvversaDaoImpl extends HibernateDao<ReazioneAvversa>
{

    public ReazioneAvversaDaoImpl(DatabaseSession.DatabaseSessionInstance sessionInstance) { super(sessionInstance); }

    @Override
    public void delete(ReazioneAvversa gruppo)
    {
        DBHelper.updateManyToManyOwned(gruppo.asReazioniAvversePastiRelation(), gruppo.asReazioniAvversePastiRelation(), Pasto.class, sessionInstance);
        sessionInstance.insertOrUpdate(gruppo);
    }

    @Override
    public int insert(ReazioneAvversa gruppo)
    {
        checkConstraints(gruppo);
        int ID = sessionInstance.insert(gruppo);
        DBHelper.updateOneToMany(gruppo.asReazioniAvverseDiagnosiRelation(), gruppo.asReazioniAvverseDiagnosiRelation(), Diagnosi.class, sessionInstance);
        DBHelper.updateManyToManyOwned(gruppo.asReazioniAvversePastiRelation(), gruppo.asReazioniAvversePastiRelation(), Pasto.class, sessionInstance);
        return ID;
    }

    @Override
    public void update(ReazioneAvversa gruppo)
    {
        checkConstraints(gruppo);
        ReazioneAvversa dbEntity = sessionInstance.getByID(ReazioneAvversa.class, gruppo.getID());

        if(dbEntity != null)
        {
            DBHelper.updateOneToMany(gruppo.asReazioniAvverseDiagnosiRelation(), gruppo.asReazioniAvverseDiagnosiRelation(), Diagnosi.class, sessionInstance);
            DBHelper.updateManyToManyOwned(gruppo.asReazioniAvversePastiRelation(), gruppo.asReazioniAvversePastiRelation(), Pasto.class, sessionInstance);
            sessionInstance.insertOrUpdate(gruppo);
        }
        else
            insert(gruppo);

    }

    private void checkConstraints(ReazioneAvversa gruppo)
    {
        if(gruppo.getNome() == null)
            throw new RuntimeException("Nome Null!");
    }
}
