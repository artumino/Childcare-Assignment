package com.polimi.childcare.server.handlers.entities.getters;

import com.polimi.childcare.shared.entities.Contatto;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredContattoRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListContattoResponse;

import java.util.ArrayList;

public class FilteredContattoRequestHandler extends FilteredRequestHandler<FilteredContattoRequest, Contatto>
{
    @Override
    public BaseResponse processRequest(FilteredContattoRequest request)
    {
        if(request.getCount() < 0 || request.getPageNumber() < 0)
            return new BadRequestResponse();

        return new ListContattoResponse(200, getFilteredResult(request, Contatto.class, new ArrayList<>()));
    }
}