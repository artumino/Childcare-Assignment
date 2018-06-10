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
    public void delete(MezzoDiTrasporto gruppo)
    {
        MezzoDiTrasporto dbEntity = sessionInstance.getByID(MezzoDiTrasporto.class, gruppo.getID());
        Set<PianoViaggi> pv = dbEntity.getPianoViaggi();

        for (PianoViaggi p : pv)
        {
            p.setMezzo(null);
            sessionInstance.update(p);
        }

        sessionInstance.delete(gruppo);
    }

    @Override
    public int insert(MezzoDiTrasporto gruppo)
    {
        checkConstraints(gruppo);
        int ID = sessionInstance.insert(gruppo);
        DBHelper.updateOneToMany(gruppo.asMezzoDiTrasportoPianiViaggo(), gruppo.asMezzoDiTrasportoPianiViaggo(), PianoViaggi.class, sessionInstance);
        DBHelper.updateManyToOne(gruppo.asMezziDiTrasportoFornitoreRelation(), Fornitore.class, sessionInstance);
        return ID;
    }

    @Override
    public void update(MezzoDiTrasporto gruppo)
    {
        checkConstraints(gruppo);
        MezzoDiTrasporto dbEntity = sessionInstance.getByID(MezzoDiTrasporto.class, gruppo.getID());

        if(dbEntity != null)
        {
            DBHelper.updateOneToMany(gruppo.asMezzoDiTrasportoPianiViaggo(), dbEntity.asMezzoDiTrasportoPianiViaggo(), PianoViaggi.class, sessionInstance);
            DBHelper.updateManyToOne(gruppo.asMezziDiTrasportoFornitoreRelation(), Fornitore.class, sessionInstance);
            sessionInstance.insertOrUpdate(gruppo);
        }
        else
            insert(gruppo);
    }

    private void checkConstraints(MezzoDiTrasporto gruppo)
    {
        if(gruppo.getFornitore() == null || gruppo.getTarga() == null)
            throw new RuntimeException("Fornitore e/o Targa Nullo!");
    }
}
