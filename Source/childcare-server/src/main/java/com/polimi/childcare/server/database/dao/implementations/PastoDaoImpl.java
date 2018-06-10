package com.polimi.childcare.server.database.dao.implementations;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.database.dao.HibernateDao;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.shared.entities.Fornitore;
import com.polimi.childcare.shared.entities.Menu;
import com.polimi.childcare.shared.entities.Pasto;
import com.polimi.childcare.shared.entities.ReazioneAvversa;

import java.util.Set;

public class PastoDaoImpl extends HibernateDao<Pasto>
{
    public PastoDaoImpl(DatabaseSession.DatabaseSessionInstance sessionInstance) { super(sessionInstance); }

    @Override
    public void delete(Pasto gruppo)
    {
        Pasto dbEntity = sessionInstance.getByID(Pasto.class, gruppo.getID());
        DBHelper.deletedManyToManyOwned(gruppo.asPastoMenuRelation(), dbEntity.asPastoMenuRelation(), Menu.class, sessionInstance);

        Set<ReazioneAvversa> reazioni = dbEntity.getReazione();

        for (ReazioneAvversa r : reazioni)
            dbEntity.removeReazione(r);

        sessionInstance.delete(gruppo);
    }

    @Override
    public int insert(Pasto gruppo)
    {
        checkConstraints(gruppo);
        int ID = sessionInstance.insert(gruppo);
        DBHelper.updateManyToOne(gruppo.asPastoFornitoreRelation(), Fornitore.class, sessionInstance);
        DBHelper.updateManyToManyOwned(gruppo.asPastoMenuRelation(), gruppo.asPastoMenuRelation(), Menu.class, sessionInstance);
        DBHelper.updateManyToManyOwner(gruppo.asPastoReazioniAvverseRelation(), ReazioneAvversa.class, sessionInstance);
        return ID;
    }

    @Override
    public void update(Pasto gruppo)
    {
        checkConstraints(gruppo);
        Pasto dbEntity = sessionInstance.getByID(Pasto.class, gruppo.getID());

        if(dbEntity != null)
        {
            DBHelper.updateManyToOne(gruppo.asPastoFornitoreRelation(), Fornitore.class, sessionInstance);
            DBHelper.updateManyToManyOwned(gruppo.asPastoMenuRelation(), dbEntity.asPastoMenuRelation(), Menu.class, sessionInstance);
            DBHelper.updateManyToManyOwner(gruppo.asPastoReazioniAvverseRelation(), ReazioneAvversa.class, sessionInstance);
            sessionInstance.insertOrUpdate(gruppo);
        }
        else
            insert(gruppo);

    }

    private void checkConstraints(Pasto gruppo)
    {
        if(gruppo.getFornitore() == null || gruppo.getNome() == null)
            throw new RuntimeException("Fornitore e/o Nome Nullo!");
    }
}
