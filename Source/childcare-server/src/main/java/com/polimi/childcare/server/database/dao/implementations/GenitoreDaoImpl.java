package com.polimi.childcare.server.database.dao.implementations;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.database.dao.HibernateDao;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.entities.Diagnosi;
import com.polimi.childcare.shared.entities.Genitore;

import java.util.Set;

public class GenitoreDaoImpl extends HibernateDao<Genitore>
{
    public GenitoreDaoImpl(DatabaseSession.DatabaseSessionInstance sessionInstance) { super(sessionInstance); }

    @Override
    public void delete(Genitore gruppo)
    {
        Genitore dbEntity = sessionInstance.getByID(Genitore.class, gruppo.getID());
        Set<Bambino> bambini = dbEntity.getBambini();

        for (Bambino b : bambini) {
            if (b.getGenitori().size() == 1)
                throw new RuntimeException("Operazione illegale, avrei dei bambini orfani!");
            b.removeGenitore(dbEntity);
            sessionInstance.update(b);
        }

        sessionInstance.delete(gruppo);
    }

    @Override
    public int insert(Genitore gruppo)
    {
        checkConstraints(gruppo);
        int ID = sessionInstance.insert(gruppo);
        DBHelper.updateManyToManyOwned(gruppo.asGenitoriBambiniRelation(), gruppo.asGenitoriBambiniRelation(), Bambino.class, sessionInstance);
        return ID;
    }

    @Override
    public void update(Genitore gruppo)
    {
        checkConstraints(gruppo);
        Genitore dbEntity = sessionInstance.getByID(Genitore.class, gruppo.getID());

        if(dbEntity != null)
        {
            DBHelper.updateManyToManyOwned(gruppo.asGenitoriBambiniRelation(), dbEntity.asGenitoriBambiniRelation(), Bambino.class, sessionInstance);
            sessionInstance.insertOrUpdate(gruppo);
        }
        else
            insert(gruppo);
    }

    private void checkConstraints(Genitore gruppo)
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
