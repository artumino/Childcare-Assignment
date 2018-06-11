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

    private void checkConstraints(Addetto addetto)
    {
        if (addetto.getNome() == null ||
                addetto.getCognome() == null ||
                addetto.getCodiceFiscale() == null ||
                addetto.getDataNascita() == null ||
                addetto.getStato() == null ||
                addetto.getComune() == null ||
                addetto.getProvincia() == null ||
                addetto.getCittadinanza() == null ||
                addetto.getResidenza() == null ||
                addetto.getSesso() == null)
            throw new RuntimeException("Un campo obbligatorio è null!");
    }
}
