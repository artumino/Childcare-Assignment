package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.database.dao.DaoFactory;
import com.polimi.childcare.server.database.dao.ICommonDao;
import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.dto.DTOUtils;
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
public abstract class GenericDaoSetEntityRequestHandler<T extends SetEntityRequest<IT>, IT extends TransferableEntity> implements IRequestHandler<T>
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

    @Override
    public BaseResponse processRequest(T request)
    {
        if (request == null || request.getEntity() == null)
            return new BadRequestResponse.BadRequestResponseWithMessage("Empty request");

        final BaseResponse[] response = new BaseResponse[1];
        Throwable exception;

        //In questo caso usando le lambda exception il try & catch fa il catch solo delle espressioni che vanno in errore nella lambda expression,
        //ciò che invece scatena eccezioni sul DB scatena un altro try&catch che ritorna come risultato di execute
        try {
            exception = DatabaseSession.getInstance().execute((session) ->
            {
                //Provo ad eseguire il set dopo che ho fatto i mei check
                response[0] = requestSet(session, request.getEntity(), request.getOldHashCode(), request.isToDelete());

                return !(response[0] instanceof BadRequestResponse);
            });
        }
        catch (Throwable ex)
        {
            exception = ex;
        }

        if(exception != null)
            return new BadRequestResponse.BadRequestResponseWithMessage(exception.getMessage());

        if(response[0] instanceof ListResponse)
            DTOUtils.iterableToDTO(((ListResponse)response[0]).getPayload(), new ArrayList<>());

        return response[0];
    }

    /**
     * Gestore dei setter generico
     * @return Risposta opportuna da mandare al client
     */
    private BaseResponse requestSet( DatabaseSession.DatabaseSessionInstance session, IT entity, int oldHashCode, boolean toDelete)
    {
        List<IT> lista = new ArrayList<>();
        ICommonDao<IT> commonDao = DaoFactory.getInstance().getDao(getQueryClass(), session);
        lista.add(session.getByID(getQueryClass(), entity.getID(), true));

        if (lista.contains(entity))
        {
            if (lista.get(0).consistecyHashCode() == oldHashCode)
            {
                if (toDelete)
                    commonDao.delete(entity);
                else if(entity.getID() != 0)
                    commonDao.update(entity);
                else
                    commonDao.insert(entity);
            }
            else
                return new ListResponse<>(100, lista);
        }
        else {
            if (toDelete)
                return new BadRequestResponse();
            else if(entity.getID() != 0)
                commonDao.update(entity);
            else
                commonDao.insert(entity);
        }

        return new BaseResponse(200);
    }
}
