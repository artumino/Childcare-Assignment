package com.polimi.childcare.server.database.dao.implementations;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.database.dao.HibernateDao;
import com.polimi.childcare.shared.entities.Fornitore;

public class FornitoreDaoImpl extends HibernateDao<Fornitore>
{
    public FornitoreDaoImpl(DatabaseSession.DatabaseSessionInstance sessionInstance) { super(sessionInstance); }

    @Override
    public void delete(Fornitore item)
    {
        sessionInstance.delete(item);
    }

    @Override
    public int insert(Fornitore item)
    {
        checkConstraints(item);
        int ID = sessionInstance.insert(item);
        return ID;
    }

    @Override
    public void update(Fornitore item)
    {
        checkConstraints(item);
        Fornitore dbEntity = sessionInstance.getByID(Fornitore.class, item.getID());

        if(dbEntity != null)
            sessionInstance.insertOrUpdate(item);
        else
            insert(item);

    }

    private void checkConstraints(Fornitore gruppo)
    {
        if (gruppo.getRagioneSociale() == null ||
                gruppo.getPartitaIVA() == null ||
                gruppo.getSedeLegale() == null ||
                gruppo.getNumeroRegistroImprese() == null)
            throw new RuntimeException("Un campo obbligatorio è null!");
    }
}
