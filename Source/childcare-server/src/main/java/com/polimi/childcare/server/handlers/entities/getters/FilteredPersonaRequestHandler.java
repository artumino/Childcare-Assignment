package com.polimi.childcare.server.handlers.entities.getters;

import com.polimi.childcare.shared.entities.Persona;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredPersonaRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListPersoneResponse;

import java.util.ArrayList;

public class FilteredPersonaRequestHandler extends FilteredRequestHandler<FilteredPersonaRequest, Persona>
{
    @Override
    public BaseResponse processRequest(FilteredPersonaRequest request)
    {
        if(request.getCount() < 0 || request.getPageNumber() < 0)
            return new BadRequestResponse();

        return new ListPersoneResponse(200, getFilteredResult(request, Persona.class, new ArrayList<>()));
    }
}
