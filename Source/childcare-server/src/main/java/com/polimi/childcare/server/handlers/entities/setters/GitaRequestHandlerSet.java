package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.shared.entities.Gita;
import com.polimi.childcare.shared.entities.PianoViaggi;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;

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
                    request.getEntity().getLuogo() == null)
                throw new RuntimeException("Un campo obbligatorio è null!");

            //Se ho rimosso dei piano viaggi, allora li cancello
            if(dbEntity != null)
            {
                for(PianoViaggi pianoViaggi : dbEntity.getPianiViaggi())
                {
                    if(!request.getEntity().getPianiViaggi().contains(pianoViaggi))
                        session.delete(pianoViaggi);
                }
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
