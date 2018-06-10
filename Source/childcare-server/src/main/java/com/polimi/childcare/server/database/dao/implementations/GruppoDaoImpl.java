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
    public void delete(Gruppo item)
    {
        Gruppo dbEntity = sessionInstance.getByID(Gruppo.class, item.getID());
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

        sessionInstance.delete(item);
    }

    @Override
    public int insert(Gruppo item)
    {
        checkConstraints(item);
        int ID = sessionInstance.insert(item);
        DBHelper.updateOneToMany(item.asGruppoBambiniRelation(), item.asGruppoBambiniRelation(), Bambino.class, sessionInstance);
        return ID;
    }

    @Override
    public void update(Gruppo item)
    {
        checkConstraints(item);
        Gruppo dbEntity = sessionInstance.getByID(Gruppo.class, item.getID());

        //Dato che gruppo usa un ID generato ID è sempre diverso da 0, quindi nelle insert l'unico modo è controllare se esiste sul DB
        if(dbEntity != null)
        {
            DBHelper.updateOneToMany(item.asGruppoBambiniRelation(), dbEntity.asGruppoBambiniRelation(), Bambino.class, sessionInstance);
            sessionInstance.insertOrUpdate(item);
        }
        else
            insert(item);
    }

    private void checkConstraints(Gruppo gruppo)
    {
        if (gruppo.getSorvergliante() == null)
            throw new RuntimeException("Sorvegliante Nullo!");
    }
}
