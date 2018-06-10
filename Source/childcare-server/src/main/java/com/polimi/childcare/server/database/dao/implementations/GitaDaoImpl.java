package com.polimi.childcare.server.database.dao.implementations;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.database.dao.HibernateDao;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.shared.entities.Gita;
import com.polimi.childcare.shared.entities.PianoViaggi;
import com.polimi.childcare.shared.entities.RegistroPresenze;

import java.util.Set;

public class GitaDaoImpl extends HibernateDao<Gita>
{

    public GitaDaoImpl(DatabaseSession.DatabaseSessionInstance sessionInstance) { super(sessionInstance); }

    @Override
    public void delete(Gita gruppo)
    {
        Gita dbEntity = sessionInstance.getByID(Gita.class, gruppo.getID());
        Set<PianoViaggi> pianoViaggi = dbEntity.getPianiViaggi();
        for (PianoViaggi piano : pianoViaggi)
        {
            dbEntity.unsafeRemovePianoViaggi(piano);
            piano.setGruppo(null);
            sessionInstance.delete(piano);
        }
        sessionInstance.delete(gruppo);
    }

    @Override
    public int insert(Gita gruppo)
    {
        checkConstraints(gruppo);
        int ID = sessionInstance.insert(gruppo);
        DBHelper.updateOneToMany(gruppo.asGitaRegistroPresenzeRelation(), gruppo.asGitaRegistroPresenzeRelation(), RegistroPresenze.class, sessionInstance);
        DBHelper.updateOneToMany(gruppo.asGitaPianiViaggioRelation(), gruppo.asGitaPianiViaggioRelation(), PianoViaggi.class, sessionInstance);
        return ID;
    }

    @Override
    public void update(Gita gruppo)
    {
        checkConstraints(gruppo);
        Gita dbEntity = sessionInstance.getByID(Gita.class, gruppo.getID());

        if(dbEntity != null)
        {
            DBHelper.updateOneToMany(gruppo.asGitaRegistroPresenzeRelation(), dbEntity.asGitaRegistroPresenzeRelation(), RegistroPresenze.class, sessionInstance);
            for(PianoViaggi pianoViaggi : dbEntity.getPianiViaggi())
            {
                if(!gruppo.getPianiViaggi().contains(pianoViaggi))
                    sessionInstance.delete(pianoViaggi);
            }
            DBHelper.updateOneToMany(gruppo.asGitaPianiViaggioRelation(), dbEntity.asGitaPianiViaggioRelation(), PianoViaggi.class, sessionInstance);
            sessionInstance.insertOrUpdate(gruppo);
        }
        else
            insert(gruppo);
    }

    private void checkConstraints(Gita gruppo)
    {
        if (gruppo.getDataInizio() == null ||
                gruppo.getDataFine() == null ||
                gruppo.getLuogo() == null)
            throw new RuntimeException("Un campo obbligatorio è null!");
    }
}
