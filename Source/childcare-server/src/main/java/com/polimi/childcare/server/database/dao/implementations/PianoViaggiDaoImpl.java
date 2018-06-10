package com.polimi.childcare.server.database.dao.implementations;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.database.dao.HibernateDao;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.shared.entities.Gita;
import com.polimi.childcare.shared.entities.Gruppo;
import com.polimi.childcare.shared.entities.MezzoDiTrasporto;
import com.polimi.childcare.shared.entities.PianoViaggi;

public class PianoViaggiDaoImpl extends HibernateDao<PianoViaggi>
{
    public PianoViaggiDaoImpl(DatabaseSession.DatabaseSessionInstance sessionInstance) { super(sessionInstance); }

    @Override
    public void delete(PianoViaggi gruppo)
    {
        sessionInstance.delete(gruppo);
    }

    @Override
    public int insert(PianoViaggi gruppo)
    {
        checkConstraints(gruppo);
        int ID = sessionInstance.insert(gruppo);
        DBHelper.updateManyToOne(gruppo.asPianiViaggioGitaRelation(), Gita.class, sessionInstance);
        DBHelper.updateManyToOne(gruppo.asPianiViaggioGruppoRelation(), Gruppo.class, sessionInstance);
        DBHelper.updateManyToOne(gruppo.asPianiViaggioMezzoDiTrasportoRelation(), MezzoDiTrasporto.class, sessionInstance);
        return ID;
    }

    @Override
    public void update(PianoViaggi gruppo)
    {
        checkConstraints(gruppo);
        PianoViaggi dbEntity = sessionInstance.getByID(PianoViaggi.class, gruppo.getID());

        if(dbEntity != null)
        {
            DBHelper.updateManyToOne(gruppo.asPianiViaggioGitaRelation(), Gita.class, sessionInstance);
            DBHelper.updateManyToOne(gruppo.asPianiViaggioGruppoRelation(), Gruppo.class, sessionInstance);
            DBHelper.updateManyToOne(gruppo.asPianiViaggioMezzoDiTrasportoRelation(), MezzoDiTrasporto.class, sessionInstance);
            sessionInstance.insertOrUpdate(gruppo);
        }
        else
            insert(gruppo);
    }

    private void checkConstraints(PianoViaggi gruppo)
    {
        if(gruppo.getGruppo() == null || gruppo.getGita() == null)
            throw new RuntimeException("Gita e/o Gruppo nulli!");
    }
}
