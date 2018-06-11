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
    public void delete(Genitore item)
    {
        Genitore dbEntity = sessionInstance.getByID(Genitore.class, item.getID());
        Set<Bambino> bambini = dbEntity.getBambini();

        for (Bambino b : bambini) {
            if (b.getGenitori().size() == 1)
                throw new RuntimeException("Operazione illegale, avrei dei bambini orfani!");
            b.removeGenitore(dbEntity);
            sessionInstance.update(b);
        }

        sessionInstance.delete(item);
    }

    @Override
    public int insert(Genitore item)
    {
        checkConstraints(item);
        int ID = sessionInstance.insert(item);
        //DBHelper.updateOneToMany(item.asPersonaDiagnosiRelation(), item.asPersonaDiagnosiRelation(), Diagnosi.class, sessionInstance);
        DBHelper.updateManyToManyOwned(item.asGenitoriBambiniRelation(), item.asGenitoriBambiniRelation(), Bambino.class, sessionInstance);
        return ID;
    }

    @Override
    public void update(Genitore item)
    {
        checkConstraints(item);
        Genitore dbEntity = sessionInstance.getByID(Genitore.class, item.getID());

        if(dbEntity != null)
        {
            DBHelper.updateManyToManyOwned(item.asGenitoriBambiniRelation(), dbEntity.asGenitoriBambiniRelation(), Bambino.class, sessionInstance);
            //DBHelper.updateOneToMany(item.asPersonaDiagnosiRelation(), dbEntity.asPersonaDiagnosiRelation(), Diagnosi.class, sessionInstance);
            sessionInstance.insertOrUpdate(item);
        }
        else
            insert(item);
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
