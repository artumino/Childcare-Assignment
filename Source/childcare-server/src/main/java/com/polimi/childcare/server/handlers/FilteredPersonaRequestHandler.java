package com.polimi.childcare.server.handlers;

import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.Persona;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredPersonaRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListPersoneResponse;

import java.util.ArrayList;

public class FilteredPersonaRequestHandler implements IRequestHandler<FilteredPersonaRequest>
{
    @Override
    public BaseResponse processRequest(FilteredPersonaRequest request)
    {
        if(request.getCount() < 0 || request.getPageNumber() < 0)
            return new BadRequestResponse();

        return new ListPersoneResponse(200, FilteredRequestHandler.requestManager(request, Persona.class, new ArrayList<Persona>()));
    }
}
