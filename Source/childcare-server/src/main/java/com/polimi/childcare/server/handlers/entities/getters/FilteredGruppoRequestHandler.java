package com.polimi.childcare.server.handlers.entities.getters;

import com.polimi.childcare.shared.entities.Gruppo;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredGruppoRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListGruppoResponse;

import java.util.ArrayList;

public class FilteredGruppoRequestHandler extends FilteredRequestHandler<FilteredGruppoRequest, Gruppo>
{
    @Override
    public BaseResponse processRequest(FilteredGruppoRequest request)
    {
        if(request.getCount() < 0 || request.getPageNumber() < 0)
            return new BadRequestResponse();

        return new ListGruppoResponse(200, getFilteredResult(request, Gruppo.class, new ArrayList<>()));
    }
}
