package com.polimi.childcare.server.database.dao.implementations;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.database.dao.HibernateDao;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.shared.entities.Gita;
import com.polimi.childcare.shared.entities.PianoViaggi;
import com.polimi.childcare.shared.entities.Tappa;
import com.polimi.childcare.shared.utils.ContainsHelper;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GitaDaoImpl extends HibernateDao<Gita>
{

    public GitaDaoImpl(DatabaseSession.DatabaseSessionInstance sessionInstance) { super(sessionInstance); }

    @Override
    public void delete(Gita item)
    {
        Gita dbEntity = sessionInstance.getByID(Gita.class, item.getID());
        //Rimuovo i piano viaggi legati a questo gruppo
        for(PianoViaggi viaggi : item.getPianiViaggi())
            item.unsafeRemovePianoViaggi(viaggi);

        for(Tappa tappa : item.getTappe())
            item.unsafeRemoveTappa(tappa);

        Set<PianoViaggi> pianoViaggi = dbEntity.getPianiViaggi();
        for (PianoViaggi piano : pianoViaggi)
        {
            dbEntity.unsafeRemovePianoViaggi(piano);
            piano.setGruppo(null);
            sessionInstance.delete(piano);
        }

        Set<Tappa> tappe = dbEntity.getTappe();
        for (Tappa tappa : tappe)
        {
            dbEntity.unsafeRemoveTappa(tappa);
            tappa.setGita(null);
            sessionInstance.delete(tappa);
        }

        sessionInstance.delete(item);
    }

    @Override
    public int insert(Gita item)
    {
        checkConstraints(item);

        //Non posso avere tappe nell'inserimento
        Set<Tappa> tappe = item.getTappe();
        if(tappe != null)
            for (Tappa tappa : tappe)
                item.unsafeRemoveTappa(tappa);

        return sessionInstance.insert(item);
    }

    @Override
    public void update(Gita item)
    {
        checkConstraints(item);
        Gita dbEntity = sessionInstance.getByID(Gita.class, item.getID());

        if(dbEntity != null)
        {
            for(PianoViaggi pianoViaggi : dbEntity.getPianiViaggi())
            {
                if(!item.getPianiViaggi().contains(pianoViaggi))
                    sessionInstance.delete(pianoViaggi);
            }

            for(Tappa tappa : dbEntity.getTappe())
                if(!ContainsHelper.containsHashCode(item.getTappe() ,tappa))
                    sessionInstance.delete(tappa);

            for (Tappa tappa : item.getTappe())
                tappa.setGita(item);

            sessionInstance.insertOrUpdate(item);
        }
        else
            insert(item);
    }

    private void checkConstraints(Gita gita)
    {
        if (gita.getDataInizio() == null ||
                gita.getDataFine() == null ||
                gita.getLuogo() == null)
            throw new RuntimeException("Un campo obbligatorio è null!");

        if(gita.getDataFine().isBefore(gita.getDataInizio()))
            throw new RuntimeException("Il campo data inizio deve essere minore o uguale a data fine!");

        List<Gita> gite = new ArrayList<>();
        CollectionUtils.addAll(gite, sessionInstance.stream(Gita.class).iterator());

        for (Gita g : gite)
        {
            if(g.getID() == gita.getID())
                continue;

            if(gita.getDataInizio().isBefore(g.getDataInizio()) || gita.getDataInizio().isEqual(g.getDataInizio()))
            {
                if(gita.getDataFine().isAfter(g.getDataInizio()) || gita.getDataFine().isEqual(g.getDataInizio()))
                    throw new RuntimeException("Overlapping di Gite!");
            }
            else
            {
                if(gita.getDataInizio().isBefore(g.getDataFine()) && gita.getDataInizio().isAfter(g.getDataInizio()))
                    throw new RuntimeException("Overlapping di Gite!");
            }
        }
    }
}
