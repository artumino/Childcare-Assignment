package com.polimi.childcare.server.database.dao.implementations;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.database.dao.HibernateDao;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.shared.entities.Fornitore;
import com.polimi.childcare.shared.entities.MezzoDiTrasporto;
import com.polimi.childcare.shared.entities.PianoViaggi;

import java.util.Set;

public class MezzoDiTrasportoDaoImpl extends HibernateDao<MezzoDiTrasporto>
{
    public MezzoDiTrasportoDaoImpl(DatabaseSession.DatabaseSessionInstance sessionInstance) { super(sessionInstance); }

    @Override
    public void delete(MezzoDiTrasporto item)
    {
        MezzoDiTrasporto dbEntity = sessionInstance.getByID(MezzoDiTrasporto.class, item.getID());
        Set<PianoViaggi> pv = dbEntity.getPianoViaggi();

        for (PianoViaggi p : pv)
        {
            p.setMezzo(null);
            sessionInstance.update(p);
        }

        sessionInstance.delete(item);
    }

    @Override
    public int insert(MezzoDiTrasporto item)
    {
        checkConstraints(item);
        int ID = sessionInstance.insert(item);
        DBHelper.updateManyToOne(item.asMezziDiTrasportoFornitoreRelation(), Fornitore.class, sessionInstance);
        return ID;
    }

    @Override
    public void update(MezzoDiTrasporto item)
    {
        checkConstraints(item);
        MezzoDiTrasporto dbEntity = sessionInstance.getByID(MezzoDiTrasporto.class, item.getID());

        if(dbEntity != null)
        {
            DBHelper.updateManyToOne(item.asMezziDiTrasportoFornitoreRelation(), Fornitore.class, sessionInstance);
            sessionInstance.insertOrUpdate(item);
        }
        else
            insert(item);
    }

    private void checkConstraints(MezzoDiTrasporto mezzoDiTrasporto)
    {
        if(mezzoDiTrasporto.getFornitore() == null || mezzoDiTrasporto.getTarga() == null)
            throw new RuntimeException("Fornitore e/o Targa Nullo!");
    }
}
