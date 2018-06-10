package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.shared.entities.Gita;
import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.shared.entities.Gita;
import com.polimi.childcare.shared.entities.PianoViaggi;
import com.polimi.childcare.shared.entities.Tappa;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GitaRequestHandlerSet extends GenericSetEntityRequestHandler<SetEntityRequest<Gita>, Gita>
{
    @Override
    protected Class<Gita> getQueryClass() {
        return Gita.class;
    }

    @Override
    protected void doPreSetChecks(DatabaseSession.DatabaseSessionInstance session, SetEntityRequest<Gita> request, Gita dbEntity)
    {
        if(!request.isToDelete())
        {
            if (request.getEntity().getDataInizio() == null ||
                    request.getEntity().getDataFine() == null ||
                    request.getEntity().getLuogo() == null ||
                    request.getEntity().getDataFine().isBefore(request.getEntity().getDataInizio()))
                throw new RuntimeException("Un campo obbligatorio è null!");

            DBHelper.updateOneToMany(request.getEntity().asGitaTappeRelation(), request.getEntity().asGitaTappeRelation(), Tappa.class, session);

            List<Gita> gite = new ArrayList<>();
            Gita nuova = request.getEntity();
            CollectionUtils.addAll(gite, session.stream(Gita.class).iterator());

            for (Gita g : gite)
            {
                if(nuova.getDataInizio().isBefore(g.getDataInizio()))
                {
                    if(nuova.getDataFine().isBefore(g.getDataFine()) || nuova.getDataFine().isAfter(g.getDataFine()))
                        throw new RuntimeException("Overlapping di Gite!");
                }
                else
                {
                    if(nuova.getDataInizio().isBefore(g.getDataFine()) && nuova.getDataInizio().isAfter(g.getDataInizio()))
                        throw new RuntimeException("Overlapping di Gite!");
                }
            }

            //Se ho rimosso dei piano viaggi, allora li cancello
            if(dbEntity != null)
            {
                for(PianoViaggi pianoViaggi : dbEntity.getPianiViaggi())
                {
                    if(!request.getEntity().getPianiViaggi().contains(pianoViaggi))
                        session.delete(pianoViaggi);
                }
                DBHelper.updateOneToMany(request.getEntity().asGitaTappeRelation(), dbEntity.asGitaTappeRelation(), Tappa.class, session);
            }
        }
        else
        {
            //Rimuovo i piano viaggi legati a questo gruppo
            for(PianoViaggi viaggi : request.getEntity().getPianiViaggi())
                request.getEntity().unsafeRemovePianoViaggi(viaggi);

            Set<PianoViaggi> pianoViaggi = dbEntity.getPianiViaggi();
            for (PianoViaggi piano : pianoViaggi)
            {
                dbEntity.unsafeRemovePianoViaggi(piano);
                piano.setGruppo(null);
                session.delete(piano);
            }
        }
    }
}
