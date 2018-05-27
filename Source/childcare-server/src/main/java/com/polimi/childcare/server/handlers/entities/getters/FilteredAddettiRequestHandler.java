package com.polimi.childcare.server.handlers.entities.getters;

import com.polimi.childcare.shared.entities.Addetto;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredAddettoRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListAddettiResponse;

import java.util.ArrayList;

public class FilteredAddettiRequestHandler extends FilteredRequestHandler <FilteredAddettoRequest, Addetto>
{
    @Override
    public BaseResponse processRequest(FilteredAddettoRequest request)
    {
        if(request.getCount() < 0 || request.getPageNumber() < 0)
            return new BadRequestResponse();

        return new ListAddettiResponse(200, getFilteredResult(request, Addetto.class, new ArrayList<>()));
    }
}