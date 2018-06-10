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
    public void delete(Diagnosi item)
    {
        sessionInstance.delete(item);
    }

    @Override
    public int insert(Diagnosi item)
    {
        checkConstraints(item);
        int ID = sessionInstance.insert(item);
        DBHelper.updateManyToOne(item.asDiagnosiPersonaRelation(), Persona.class, sessionInstance);
        DBHelper.updateManyToOne(item.asDiagnosiReazioneAvversaRelation(), ReazioneAvversa.class, sessionInstance);
        return ID;
    }

    @Override
    public void update(Diagnosi item)
    {
        checkConstraints(item);
        Diagnosi dbEntity = sessionInstance.getByID(Diagnosi.class, item.getID());

        if(dbEntity != null)
        {
            DBHelper.updateManyToOne(item.asDiagnosiPersonaRelation(), Persona.class, sessionInstance);
            DBHelper.updateManyToOne(item.asDiagnosiReazioneAvversaRelation(), ReazioneAvversa.class, sessionInstance);
            sessionInstance.insertOrUpdate(item);
        }
        else
            insert(item);

    }

    private void checkConstraints(Diagnosi gruppo)
    {
        if(gruppo.getPersona() == null || gruppo.getReazioneAvversa() == null)
            throw new RuntimeException("Persona e/o Reazione nulli!");
    }
}
