package com.polimi.childcare.server.database.dao.implementations;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.database.dao.HibernateDao;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.shared.entities.Diagnosi;
import com.polimi.childcare.shared.entities.Persona;
import com.polimi.childcare.shared.entities.ReazioneAvversa;

public class DiagnosiDaoImpl extends HibernateDao<Diagnosi>
{
    public DiagnosiDaoImpl(DatabaseSession.DatabaseSessionInstance sessionInstance) { super(sessionInstance); }

    @Override
    public void delete(Diagnosi gruppo)
    {
        sessionInstance.delete(gruppo);
    }

    @Override
    public int insert(Diagnosi gruppo)
    {
        checkConstraints(gruppo);
        int ID = sessionInstance.insert(gruppo);
        DBHelper.updateManyToOne(gruppo.asDiagnosiPersonaRelation(), Persona.class, sessionInstance);
        DBHelper.updateManyToOne(gruppo.asDiagnosiReazioneAvversaRelation(), ReazioneAvversa.class, sessionInstance);
        return ID;
    }

    @Override
    public void update(Diagnosi gruppo)
    {
        checkConstraints(gruppo);
        Diagnosi dbEntity = sessionInstance.getByID(Diagnosi.class, gruppo.getID());

        if(dbEntity != null)
        {
            DBHelper.updateManyToOne(gruppo.asDiagnosiPersonaRelation(), Persona.class, sessionInstance);
            DBHelper.updateManyToOne(gruppo.asDiagnosiReazioneAvversaRelation(), ReazioneAvversa.class, sessionInstance);
            sessionInstance.insertOrUpdate(gruppo);
        }
        else
            insert(gruppo);

    }

    private void checkConstraints(Diagnosi gruppo)
    {
        if(gruppo.getPersona() == null || gruppo.getReazioneAvversa() == null)
            throw new RuntimeException("Persona e/o Reazione nulli!");
    }
}
