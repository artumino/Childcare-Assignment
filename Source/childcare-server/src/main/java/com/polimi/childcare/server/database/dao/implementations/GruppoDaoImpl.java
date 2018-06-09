package com.polimi.childcare.server.database.dao.implementations;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.database.dao.HibernateDao;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.entities.Gruppo;
import com.polimi.childcare.shared.entities.PianoViaggi;

import java.util.Set;

public class GruppoDaoImpl extends HibernateDao<Gruppo>
{
    public GruppoDaoImpl(DatabaseSession.DatabaseSessionInstance sessionInstance) {
        super(sessionInstance);
    }

    @Override
    public void delete(Gruppo gruppo)
    {
        Gruppo dbEntity = sessionInstance.getByID(Gruppo.class, gruppo.getID());
        Set<Bambino> bambini = dbEntity.getBambini();
        for (Bambino b : bambini)
        {
            b.setGruppo(null);
            sessionInstance.update(b);
        }

        Set<PianoViaggi> pianoViaggi = dbEntity.getPianoviaggi();
        for (PianoViaggi piano : pianoViaggi)
        {
            dbEntity.unsafeRemovePianoViaggi(piano);
            piano.setGruppo(null);
            sessionInstance.delete(piano);
        }

        sessionInstance.delete(gruppo);
    }

    @Override
    public int insert(Gruppo gruppo)
    {
        checkConstraints(gruppo);
        int ID = sessionInstance.insert(gruppo);
        DBHelper.updateOneToMany(gruppo.asGruppoBambiniRelation(), gruppo.asGruppoBambiniRelation(), Bambino.class, sessionInstance);
        return ID;
    }

    @Override
    public void update(Gruppo gruppo)
    {
        checkConstraints(gruppo);
        Gruppo dbEntity = sessionInstance.getByID(Gruppo.class, gruppo.getID());

        //Dato che gruppo usa un ID generato ID è sempre diverso da 0, quindi nelle insert l'unico modo è controllare se esiste sul DB
        if(dbEntity != null)
        {
            DBHelper.updateOneToMany(gruppo.asGruppoBambiniRelation(), dbEntity.asGruppoBambiniRelation(), Bambino.class, sessionInstance);
            sessionInstance.insertOrUpdate(gruppo);
        }
        else
            insert(gruppo);
    }

    private void checkConstraints(Gruppo gruppo)
    {
        if (gruppo.getSorvergliante() == null)
            throw new RuntimeException("Sorvegliante Nullo!");
    }
}
