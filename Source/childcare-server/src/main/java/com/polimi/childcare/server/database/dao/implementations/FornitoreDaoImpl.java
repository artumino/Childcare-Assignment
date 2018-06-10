package com.polimi.childcare.server.database.dao.implementations;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.database.dao.HibernateDao;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.shared.entities.Fornitore;
import com.polimi.childcare.shared.entities.MezzoDiTrasporto;
import com.polimi.childcare.shared.entities.Pasto;

public class FornitoreDaoImpl extends HibernateDao<Fornitore>
{
    public FornitoreDaoImpl(DatabaseSession.DatabaseSessionInstance sessionInstance) { super(sessionInstance); }

    @Override
    public void delete(Fornitore gruppo)
    {
        delete(gruppo); //FIXME
    }

    @Override
    public int insert(Fornitore gruppo)
    {
        checkConstraints(gruppo);
        int ID = sessionInstance.insert(gruppo);
        return ID;
    }

    @Override
    public void update(Fornitore gruppo)
    {
        checkConstraints(gruppo);
        Fornitore dbEntity = sessionInstance.getByID(Fornitore.class, gruppo.getID());

        if(dbEntity != null)
            sessionInstance.insertOrUpdate(gruppo);
        else
            insert(gruppo);

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
