package com.polimi.childcare.server.database.dao.implementations;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.database.dao.HibernateDao;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.shared.entities.Addetto;
import com.polimi.childcare.shared.entities.Diagnosi;

public class AddettoDaoImpl extends HibernateDao<Addetto>
{
    public AddettoDaoImpl(DatabaseSession.DatabaseSessionInstance sessionInstance) {
        super(sessionInstance);
    }

    @Override
    public void delete(Addetto gruppo)
    {
        Addetto dbEntity = sessionInstance.getByID(Addetto.class, gruppo.getID());
        sessionInstance.delete(gruppo);
    }

    @Override
    public int insert(Addetto gruppo)
    {
        checkConstraints(gruppo);
        int ID = sessionInstance.insert(gruppo);
        return ID;
    }

    @Override
    public void update(Addetto gruppo)
    {
        checkConstraints(gruppo);
        Addetto dbEntity = sessionInstance.getByID(Addetto.class, gruppo.getID());

        if(dbEntity != null)
            sessionInstance.insertOrUpdate(gruppo);

        else
            insert(gruppo);

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
