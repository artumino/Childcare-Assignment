package com.polimi.childcare.server.handlers.entities.getters;

import com.polimi.childcare.shared.entities.Genitore;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredGenitoriRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListGenitoriResponse;

import java.util.ArrayList;

public class FilteredGenitoreRequestHandler extends FilteredRequestHandler<FilteredGenitoriRequest, Genitore>
{
    @Override
    public BaseResponse processRequest(FilteredGenitoriRequest request)
    {
        if(request.getCount() < 0 || request.getPageNumber() < 0)
            return new BadRequestResponse();

        return new ListGenitoriResponse(200, getFilteredResult(request, Genitore.class, new ArrayList<>()));
    }
}
