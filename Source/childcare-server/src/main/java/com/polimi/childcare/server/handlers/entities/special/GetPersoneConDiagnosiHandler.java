package com.polimi.childcare.server.handlers.entities.special;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.handlers.entities.getters.FilteredRequestHandler;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.dto.DTOUtils;
import com.polimi.childcare.shared.entities.Persona;
import com.polimi.childcare.shared.networking.requests.special.GetPersoneWithDisagnosiRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListPersoneResponse;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Ritorna una lista di persone non dettagliata ma con la lista delle Diagnosi e ReazioniAvverse inizializzata.
 */
public class GetPersoneConDiagnosiHandler implements IRequestHandler<GetPersoneWithDisagnosiRequest>
{
    @Override
    public BaseResponse processRequest(GetPersoneWithDisagnosiRequest request)
    {
        List<Persona> list = new ArrayList<>();
        if(request.getCount() == 1 && !request.isGroup())
            list.add(DatabaseSession.getInstance().getByID(Persona.class, request.getID(), true));
        else if (request.getCount() == 0)
            DatabaseSession.getInstance().execute(session -> {
                Stream<Persona> stream =  session.stream(Persona.class);

                CollectionUtils.addAll(list, stream.iterator());

                list.forEach(persona -> DBHelper.recursiveIterableIntitialize(persona.getDiagnosi()));
                if(request.isDetailed())
                    DBHelper.recursiveIterableIntitialize(list);
                return true;
            });

        else
            DatabaseSession.getInstance().execute(session -> {
                Stream<Persona> stream =  session.stream(Persona.class);

                CollectionUtils.addAll(list, stream.limit(request.getCount() * (request.getPageNumber() + 1)).skip(request.getCount() * request.getPageNumber()).iterator());

                list.forEach(persona -> DBHelper.recursiveIterableIntitialize(persona.getDiagnosi()));
                if(request.isDetailed())
                    DBHelper.recursiveIterableIntitialize(list);
                return true;
            });

        List<Persona> persone = list;
        persone = DTOUtils.iterableToDTO(list, new ArrayList<>());
        return new ListPersoneResponse(200, persone);
    }
}
