package com.polimi.childcare.server.database.dao.implementations;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.database.dao.HibernateDao;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.entities.Genitore;

import java.util.ArrayList;
import java.util.HashSet;
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
            sessionInstance.insertOrUpdate(item);
        }
        else
            insert(item);
    }

    private void checkConstraints(Genitore genitore)
    {
        if (genitore.getNome() == null ||
                genitore.getCognome() == null ||
                genitore.getCodiceFiscale() == null ||
                genitore.getDataNascita() == null ||
                genitore.getStato() == null ||
                genitore.getComune() == null ||
                genitore.getProvincia() == null ||
                genitore.getCittadinanza() == null ||
                genitore.getResidenza() == null ||
                genitore.getSesso() == null)
            throw new RuntimeException("Un campo obbligatorio è null!");

        /*Set<Bambino> bambini = genitore.getBambini();     //TODO: funziona se vuoi usarlo
        Bambino db;

        for (Bambino b : bambini)
        {
            db = sessionInstance.getByID(Bambino.class, b.getID());
            if (db.getGenitori().size() == 1 && db.getGenitori().contains(genitore))
                throw new RuntimeException("Operazione illegale, avrei dei bambini orfani!");
        }*/
    }
}
