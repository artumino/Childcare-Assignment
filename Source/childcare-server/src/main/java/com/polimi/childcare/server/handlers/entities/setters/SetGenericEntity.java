package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.shared.entities.TransferableEntity;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListResponse;

import java.util.ArrayList;
import java.util.List;

public class SetGenericEntity
{

    /**
     * Gestore dei setter generico
     * @param request Tipo di richiesta
     * @param classe Classe di tipo IT
     * @param <T> Enità generica
     * @return
     */
    public static <T extends TransferableEntity> BaseResponse Setter(SetEntityRequest<T> request, Class<T> classe, DatabaseSession.DatabaseSessionInstance session)
    {
        List<T> lista = new ArrayList<>();

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
