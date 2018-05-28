package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.shared.entities.*;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.utils.ContainsHelper;

import java.util.Set;

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
        //dbEntity è l'entità di tipo T che è stata presa direttamente dal DB, quella che alla fine del metodo deve essere modificata
        Set<Contatto> contattiset = request.getEntity().getContatti();

        //Controllo, se l'entità è già nel BD(quindi Update o Delete)
        if (dbEntity != null && request.getOldHashCode() == dbEntity.consistecyHashCode())
        {
            if (!request.isToDelete())
            {
                //Se è un update, devo assicurarmi che le relazioni che sono cambiate siano consistenti (ovvero che dati gli ID di cosa è cambiato, i dati delle relazioni siano quelli che ho nel DB)
                //Per bambino ho 4 relazioni:
                //Many-To-Many con Genitore (di cui sono l'owner, quindi non mi devo preoccupare di non modificare i dati dei genitori)
                //Many-To-One con Gruppo    (di cui  sono owner, quindi mi devo preoccupare di non modificarne i dati)
                //Many-To-One con Pediatra (di cui sono owner quindi mi devo preoccupare di non modificarne i dati)
                //Many-To-Many con Contatti (di cui non sono owner, quindi devo aggiornare i contatti aggiunti per avere la relazione con questo bambino e quelli rimossi per rimuovere la relazione)
                Set<Genitore> genitoreset = request.getEntity().getGenitori();

                //Qualunque sia il pediatra, se non è null prendo l'ID del nuovo pediatra e ne aggiorno i dati con quello sul DB
                if(dbEntity.getPediatra() != null)
                {
                    //Questo check risolve il caso in cui:
                    /*
                        Utente A: aggiorna in locale il Bambino senza modificare il Pediatra (ID=1)
                        Utente B: manda al DB una modifica per Pediatra con ID = 1 in cui cambia il nome al pediatra
                        Utente A: conclude le modifiche al bambino e manda la richiesta di set
                           Se non facessimo questo check, entity.getPediatra() ritornerebbe l'ID del pediatra giusto ma con le vecchie modifiche
                           e quindi l'update andrebbe a modificarne la tabella
                        Facendo il controllo, imposto il pediatra al valore che ho nel DB quindi ha ID giusto e dati giusti, così facendo l'update non sovrascrive nulla
                     */
                    Pediatra pediatraGet = session.getByID(Pediatra.class, dbEntity.getPediatra().getID());
                    request.getEntity().setPediatra(pediatraGet);
                }

                //Stessa cosa per gruppo
                if(dbEntity.getGruppo() != null)
                {
                    Gruppo gruppoGet = session.getByID(Gruppo.class, dbEntity.getGruppo().getID());
                    request.getEntity().setGruppo(gruppoGet);
                }

                //Dato che sono owner anche per tutti i genitori devo fare la stessa cosa, essendo owner non mi preoccupo di modificare la relazione per i genitori
                //dato che sarà aggiornata automaticamente
                for (Genitore unupdated : genitoreset) {
                    request.getEntity().removeGenitore(unupdated); //Rimuovo l'istanza con i dati vecchi del gentore
                    Genitore genitoreGet = session.getByID(Genitore.class, unupdated.getID()); //Ottengo i dati nuovi
                    if (genitoreGet != null) //Se i dati nuovi esistono (il genitore non è stato cancellato nel frattempo)
                        request.getEntity().addGenitore(genitoreGet); //Riaggiungo il genitore con i dati nuovi
                    //Noto che se un genitore è stato tolto dalla lista, la relazione verrà aggiornata automaticamente da Hibernate dato che sono l'owner
                }

                //Per i contatti, invece, non sono owner quindi non mi preoccupo tanto dei dati ma delle relazioni. Non avendo potere sulle relazioni devo fare l'update di ogni
                //contatto nuovo e rimosso (quelli che non sono stati toccati possono rimanere nella lista anche con dati sbgliati dato che non verranno

                for (Contatto unupdated : contattiset) //Sottraggo i contatti legati al bambino le cui relazioni non sono cambiate
                    if(ContainsHelper.containsHashCode(dbEntity.getContatti(), unupdated)) //Se il contatto è in entrambe le liste(non ho modificato alcuna relazione, allora lo rimuovo da entrambe
                    {
                        request.getEntity().unsafeRemoveContatto(unupdated);
                        dbEntity.unsafeRemoveContatto(unupdated);
                    }
                //Dopo questa operazione in request.getEntity().getContatti() ho i contatti aggiunti
                //in dbEntity.getContatti() ho quelli che ho rimosso dalla relazione
                Set<Contatto> insertedContatti = request.getEntity().getContatti();
                Set<Contatto> removedContatti = dbEntity.getContatti();

                //Per tutti i contatti aggiunti, aggiungo la relazione
                for(Contatto unupdated : insertedContatti)
                {
                    //I contatti inseriti non li ho ancora presi dal DB quindi è buona norma aspettarsi che potrebbero essere cambiati, quindi oltre ad impostare la relazione
                    //sistemo anche l'integrità dei dati facendo come per Genitore, Pediatra, Etc.
                    Contatto updated = session.getByID(Contatto.class, unupdated.getID());
                    request.getEntity().unsafeRemoveContatto(unupdated);

                    //Se non è stato cancellato il contatto inserito, allora riaggiungo la versione aggiornata e lo aggiorno sul DB con la nuova relazione
                    if (updated != null) {
                        updated.removeBambino(dbEntity);
                        session.update(updated);
                        request.getEntity().unsafeAddContatto(updated);
                    }
                }

                //Per tutti i contatti rimossi(che sono presi da dbEntity, quindi sono già consistenti sul DB mi limito a rimuovere la relazione ed aggiornarli)
                for (Contatto removed : removedContatti)
                {
                    removed.removeBambino(dbEntity);
                    session.update(removed);
                }
            }
            else
            {
                //In caso di rimozione mi devo preoccupare solo di eventuali Cascade/Constraint(che su bambino non ho)
                //e delle relazioni di cui non sono owner, in questo caso solo quella con contatti

                //Rimuove le relazioni di cui non è owner
                for (Contatto unupdated : contattiset)
                {
                    request.getEntity().unsafeRemoveContatto(unupdated);
                    Contatto updated = session.getByID(Contatto.class, unupdated.getID());
                    if (updated != null)
                    {
                        updated.removeBambino(dbEntity);
                        session.update(updated);
                    }
                }
            }
        }
    }
}