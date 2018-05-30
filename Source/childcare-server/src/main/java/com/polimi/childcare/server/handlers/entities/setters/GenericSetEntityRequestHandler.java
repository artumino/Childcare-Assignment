package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.TransferableEntity;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe per la gestione delle richieste di Set
 * @param <T> Tipo richiesta da gestire
 * @param <IT> Enità generica
 */
public abstract class GenericSetEntityRequestHandler<T extends SetEntityRequest<IT>, IT extends TransferableEntity> implements IRequestHandler<T>
{
    /*
        Procedura di set di una entità:
        1. processRequest: comune a tutti, crea una transazione, fa il getByID della entità da settare ed infine alcuni controlli di validità della richiesta
        2. doPreSetChecks: diverso per ogni handler, gestisce le operazioni per assicurarsi che i constraint sul DB della entità vengano rispettati
        3. requestSet: comune a tutti, esegue l'insert/delete/update dell'entità passata. Ritorna eccezzioni opportune in caso di errori
     */

    /**
     * @return Classe da ultilizzare per la query
     */
    protected abstract Class<IT> getQueryClass();

    /**
     * Data la richiesta, controlla che la query di set possa essere effettuata senza eccezioni
     * @param session DatabaseSession da utilizzare per le query sul DB
     * @param dbEntity Corrispondente sul DB di request.getEntity() (null se non presente)
     * @param request La richiesta da gestire e modificare per fare si che la query vada a buon fine
     */
    protected abstract void doPreSetChecks(DatabaseSession.DatabaseSessionInstance session, T request, IT dbEntity);

    @Override
    public BaseResponse processRequest(T request)
    {
        if (request == null || request.getEntity() == null)
            return new BadRequestResponse.BadRequestResponseWithMessage("Empty request");

        final BaseResponse[] response = new BaseResponse[1];
        Throwable exception = DatabaseSession.getInstance().execute((session) ->
        {
            IT dbEntity = session.getByID(getQueryClass(), request.getEntity().getID(), true);

            //Faccio i controlli prima di eseguire la query di set
            //Ritorno una request modificata con i dati opportuni per non avere problemi con il set di request.getEntity()
            doPreSetChecks(session, request, dbEntity);

            //Provo ad eseguire il set dopo che ho fatto i mei check
            response[0] = requestSet(session, request.getEntity(), dbEntity, request.getOldHashCode(), request.isToDelete());
            return !(response[0] instanceof BadRequestResponse);
        });

        if(exception != null)
            return new BadRequestResponse.BadRequestResponseWithMessage(exception.getMessage());

        return response[0];
    }

    /**
     * Gestore dei setter generico
     * @return Risposta opportuna da mandare al client
     */
    private BaseResponse requestSet( DatabaseSession.DatabaseSessionInstance session, IT entity, IT dbEntity, int oldHashCode, boolean toDelete)
    {
        List<IT> lista = new ArrayList<>();

        lista.add(dbEntity);

        if (lista.contains(entity)) {
            if (lista.get(0).consistecyHashCode() == oldHashCode) {
                if (toDelete)
                    session.delete(entity);
                else
                    session.insertOrUpdate(entity);
            } else
                return new ListResponse<>(100, lista);
        } else {
            if (toDelete)
                return new BadRequestResponse();

            else
                session.insertOrUpdate(entity);
        }

        return new BaseResponse(200);
    }
}
