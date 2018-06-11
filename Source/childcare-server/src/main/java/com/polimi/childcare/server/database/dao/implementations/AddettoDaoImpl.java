package com.polimi.childcare.server.database.dao.implementations;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.database.dao.DaoFactory;
import com.polimi.childcare.server.database.dao.HibernateDao;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.shared.entities.Addetto;
import com.polimi.childcare.shared.entities.Diagnosi;

import java.util.Set;

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


        //Tolgo le diagnosi perchè dipendono da Bambino, le inserisco dopo
        Set<Diagnosi> diagnosiDaInserire = item.getDiagnosi();
        for(Diagnosi diagnosi : diagnosiDaInserire)
            item.unsafeRemoveDiagnosi(diagnosi);

        int ID = sessionInstance.insert(item);

        for(Diagnosi diagnosi : diagnosiDaInserire)
        {
            diagnosi.setPersona(item);
            DaoFactory.getInstance().getDao(Diagnosi.class, sessionInstance).insert(diagnosi);
        }
        //DBHelper.updateOneToMany(item.asPersonaDiagnosiRelation(), item.asPersonaDiagnosiRelation(), Diagnosi.class, sessionInstance);
        return ID;
    }

    @Override
    public void update(Addetto item)
    {
        checkConstraints(item);
        Addetto dbEntity = sessionInstance.getByID(Addetto.class, item.getID());

        if(dbEntity != null)
        {
            //DBHelper.updateOneToMany(item.asPersonaDiagnosiRelation(), dbEntity.asPersonaDiagnosiRelation(), Diagnosi.class, sessionInstance);
            sessionInstance.insertOrUpdate(item);
        }
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
