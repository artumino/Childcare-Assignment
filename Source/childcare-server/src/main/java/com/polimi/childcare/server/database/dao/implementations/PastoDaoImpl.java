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
    public void delete(Pasto item)
    {
        Pasto dbEntity = sessionInstance.getByID(Pasto.class, item.getID());
        DBHelper.deletedManyToManyOwned(item.asPastoMenuRelation(), dbEntity.asPastoMenuRelation(), Menu.class, sessionInstance);

        Set<ReazioneAvversa> reazioni = dbEntity.getReazione();

        for (ReazioneAvversa r : reazioni)
            dbEntity.removeReazione(r);

        sessionInstance.delete(item);
    }

    @Override
    public int insert(Pasto item)
    {
        checkConstraints(item);
        int ID = sessionInstance.insert(item);
        DBHelper.updateManyToOne(item.asPastoFornitoreRelation(), Fornitore.class, sessionInstance);
        DBHelper.updateManyToManyOwned(item.asPastoMenuRelation(), item.asPastoMenuRelation(), Menu.class, sessionInstance);
        DBHelper.updateManyToManyOwner(item.asPastoReazioniAvverseRelation(), ReazioneAvversa.class, sessionInstance);
        return ID;
    }

    @Override
    public void update(Pasto item)
    {
        checkConstraints(item);
        Pasto dbEntity = sessionInstance.getByID(Pasto.class, item.getID());

        if(dbEntity != null)
        {
            DBHelper.updateManyToOne(item.asPastoFornitoreRelation(), Fornitore.class, sessionInstance);
            DBHelper.updateManyToManyOwned(item.asPastoMenuRelation(), dbEntity.asPastoMenuRelation(), Menu.class, sessionInstance);
            DBHelper.updateManyToManyOwner(item.asPastoReazioniAvverseRelation(), ReazioneAvversa.class, sessionInstance);
            sessionInstance.insertOrUpdate(item);
        }
        else
            insert(item);

    }

    private void checkConstraints(Pasto gruppo)
    {
        if(gruppo.getFornitore() == null || gruppo.getNome() == null)
            throw new RuntimeException("Fornitore e/o Nome Nullo!");
    }
}
