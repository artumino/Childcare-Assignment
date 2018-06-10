package com.polimi.childcare.server.database.dao.implementations;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.database.dao.HibernateDao;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.shared.entities.Gita;
import com.polimi.childcare.shared.entities.Gruppo;
import com.polimi.childcare.shared.entities.MezzoDiTrasporto;
import com.polimi.childcare.shared.entities.PianoViaggi;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

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

    public List<PianoViaggi> getPianiViaggioByGita(Gita gita)
    {
        CriteriaBuilder builder = sessionInstance.getSession().getCriteriaBuilder();
        CriteriaQuery<PianoViaggi> pianoViaggiCriteriaQuery = builder.createQuery(PianoViaggi.class);
        Root<PianoViaggi> root = pianoViaggiCriteriaQuery.from(PianoViaggi.class);
        pianoViaggiCriteriaQuery.where(builder.equal(root.get("gita"), gita));
        return sessionInstance.getSession().createQuery(pianoViaggiCriteriaQuery).getResultList();
    }

    public int deletePianiViaggioByGita(Gita gita)
    {
        CriteriaBuilder builder = sessionInstance.getSession().getCriteriaBuilder();
        CriteriaDelete<PianoViaggi> pianoViaggiCriteriaQuery = builder.createCriteriaDelete(PianoViaggi.class);
        Root<PianoViaggi> root = pianoViaggiCriteriaQuery.from(PianoViaggi.class);
        pianoViaggiCriteriaQuery.where(builder.equal(root.get("gita"), gita));
        return sessionInstance.getSession().createQuery(pianoViaggiCriteriaQuery).executeUpdate();
    }
}
