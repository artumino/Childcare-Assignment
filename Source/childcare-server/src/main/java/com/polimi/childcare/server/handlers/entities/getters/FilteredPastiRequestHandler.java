package com.polimi.childcare.server.handlers.entities.getters;

import com.polimi.childcare.shared.entities.Pasto;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredPastoRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListPastiResponse;

import java.util.ArrayList;

public class FilteredPastiRequestHandler extends FilteredRequestHandler<FilteredPastoRequest, Pasto>
{
    @Override
    public BaseResponse processRequest(FilteredPastoRequest request)
    {
        if(request.getCount() < 0 || request.getPageNumber() < 0)
            return new BadRequestResponse();

        return new ListPastiResponse(200, getFilteredResult(request, Pasto.class, new ArrayList<>()));
    }
}