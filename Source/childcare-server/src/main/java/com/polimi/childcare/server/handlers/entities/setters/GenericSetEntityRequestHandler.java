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
    /**
     * Gestore dei setter generico
     * @param request Richiesta
     * @param classe Classe di tipo IT
     * @return
     */
    public BaseResponse requestSet(T request, Class<IT> classe, DatabaseSession.DatabaseSessionInstance session)
    {
        List<IT> lista = new ArrayList<>();

        if (request == null)
            return new BadRequestResponse();
        else {
            lista.add(session.getByID(classe, request.getEntity().getID(), true));

            if (lista.contains(request.getEntity())) {
                if (lista.get(0).consistecyHashCode() == request.getOldHashCode()) {
                    if (request.isToDelete())
                        session.delete(request.getEntity());
                    else
                        session.insertOrUpdate(request.getEntity());
                } else
                    return new ListResponse<>(100, lista);
            } else {
                if (request.isToDelete())
                    return new BadRequestResponse();

                else
                    session.insertOrUpdate(request.getEntity());
            }
        }

        return new BaseResponse(200);
    }
}
