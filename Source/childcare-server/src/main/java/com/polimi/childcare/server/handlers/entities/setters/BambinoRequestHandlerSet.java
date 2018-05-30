package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.shared.entities.*;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;

public class BambinoRequestHandlerSet extends GenericSetEntityRequestHandler<SetEntityRequest<Bambino>, Bambino>
{
    @Override
    protected Class<Bambino> getQueryClass()
    {
        return Bambino.class;
    }

    @Override
    //Metodo chiamato per eseguire i controlli pre-set, dato che il set in se è sempre standard il codice per il set risiede in GenericSetEntityRequestHandler
    //insieme al codice di gestione della richiesta (anche lui sempre comune a tutti gli handler di set)
    protected void doPreSetChecks(DatabaseSession.DatabaseSessionInstance session, SetEntityRequest<Bambino> request, Bambino dbEntity)
    {
        //Controllo, se l'entità è già nel BD(quindi Update o Delete)
        if (dbEntity != null && request.getOldHashCode() == dbEntity.consistecyHashCode())
        {
            if(request.getEntity().getGenitori().size() == 0)
                throw new RuntimeException("Operazione illegale, avrei dei bambini orfani!");

            if (!request.isToDelete())
            {
                if(request.getEntity().getNome() == null ||
                        request.getEntity().getCognome() == null ||
                        request.getEntity().getCodiceFiscale() == null ||
                        request.getEntity().getDataNascita() == null ||
                        request.getEntity().getStato() == null ||
                        request.getEntity().getComune() == null ||
                        request.getEntity().getProvincia() == null ||
                        request.getEntity().getCittadinanza() == null ||
                        request.getEntity().getResidenza() == null ||
                        request.getEntity().getSesso() == null)
                    throw new RuntimeException("Un campo obbligatorio è null!");

                //Sistemo il pediatra
                DBHelper.updateManyToOne(request.getEntity().asBambiniPediatraRelation(), Pediatra.class, session);

                //Sistemo il gruppo
                DBHelper.updateManyToOne(request.getEntity().asBambiniGruppoRelation(), Gruppo.class, session);

                //Sistemo i genitori
                DBHelper.updateManyToManyOwner(request.getEntity().asBambiniGenitoriRelation(), Genitore.class, session);

                //Sistemo i contatti
                DBHelper.updateManyToManyOwned(request.getEntity().asBambiniContattiRelation(), dbEntity.asBambiniContattiRelation(), Contatto.class, session);
            }
            else
            {
                //In caso di rimozione mi devo preoccupare solo di eventuali Cascade/Constraint(che su bambino non ho)
                //e delle relazioni di cui non sono owner, in questo caso solo quella con contatti

                DBHelper.deletedManyToManyOwned(request.getEntity().asBambiniContattiRelation(), dbEntity.asBambiniContattiRelation(), Contatto.class, session);
            }
        }
    }
}