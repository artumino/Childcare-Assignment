package com.polimi.childcare.server.handlers;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.Persona;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredPersonaRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListPersoneResponse;
import java.util.ArrayList;
import java.util.List;

public class FilteredPersonaRequestHandler implements IRequestHandler<FilteredPersonaRequest>
{
    @Override
    public BaseResponse processRequest(FilteredPersonaRequest request)
    {
        if(request.getCount() < 0 || request.getPageNumber() < 0)
            return new BadRequestResponse();

        List<Persona> persone = new ArrayList<>();

        if(request.getCount() == 0)
            DatabaseSession.getInstance().execute(session -> {
                persone.addAll(session.query(Persona.class).toList());
                return true;
            });
        else
            DatabaseSession.getInstance().execute(session -> {
                persone.addAll(session.query(Persona.class).limit(request.getCount()*(request.getPageNumber() + 1)).skip(request.getCount()*request.getPageNumber()).toList());
                return true;
            });

        ListPersoneResponse risposta = new ListPersoneResponse(200, persone);

        return risposta;


    }
}
