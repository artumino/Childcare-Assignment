package com.polimi.childcare.server.database.dao.implementations;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.database.dao.HibernateDao;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.shared.entities.Pasto;
import com.polimi.childcare.shared.entities.ReazioneAvversa;

public class ReazioneAvversaDaoImpl extends HibernateDao<ReazioneAvversa>
{

    public ReazioneAvversaDaoImpl(DatabaseSession.DatabaseSessionInstance sessionInstance) { super(sessionInstance); }

    @Override
    public void delete(ReazioneAvversa item)
    {
        DBHelper.updateManyToManyOwned(item.asReazioniAvversePastiRelation(), item.asReazioniAvversePastiRelation(), Pasto.class, sessionInstance);
        sessionInstance.delete(item);
    }

    @Override
    public int insert(ReazioneAvversa item)
    {
        checkConstraints(item);
        int ID = sessionInstance.insert(item);
        DBHelper.updateManyToManyOwned(item.asReazioniAvversePastiRelation(), item.asReazioniAvversePastiRelation(), Pasto.class, sessionInstance);
        return ID;
    }

    @Override
    public void update(ReazioneAvversa item)
    {
        checkConstraints(item);
        ReazioneAvversa dbEntity = sessionInstance.getByID(ReazioneAvversa.class, item.getID());

        if(dbEntity != null)
        {
            DBHelper.updateManyToManyOwned(item.asReazioniAvversePastiRelation(), item.asReazioniAvversePastiRelation(), Pasto.class, sessionInstance);
            sessionInstance.insertOrUpdate(item);
        }
        else
            insert(item);

    }

    private void checkConstraints(ReazioneAvversa reazioneAvversa)
    {
        if(reazioneAvversa.getNome() == null)
            throw new RuntimeException("Nome Null!");
    }
}
