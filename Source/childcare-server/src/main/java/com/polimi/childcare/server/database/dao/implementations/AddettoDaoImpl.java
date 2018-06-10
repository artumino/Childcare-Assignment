package com.polimi.childcare.server.database.dao.implementations;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.database.dao.HibernateDao;
import com.polimi.childcare.shared.entities.Addetto;

public class AddettoDaoImpl extends HibernateDao<Addetto>
{
    public AddettoDaoImpl(DatabaseSession.DatabaseSessionInstance sessionInstance) {
        super(sessionInstance);
    }

    @Override
    public void delete(Addetto item)
    {
        Addetto dbEntity = sessionInstance.getByID(Addetto.class, item.getID());
        sessionInstance.delete(item);
    }

    @Override
    public int insert(Addetto item)
    {
        checkConstraints(item);
        int ID = sessionInstance.insert(item);
        return ID;
    }

    @Override
    public void update(Addetto item)
    {
        checkConstraints(item);
        Addetto dbEntity = sessionInstance.getByID(Addetto.class, item.getID());

        if(dbEntity != null)
            sessionInstance.insertOrUpdate(item);

        else
            insert(item);

    }

    private void checkConstraints(Addetto gruppo)
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
