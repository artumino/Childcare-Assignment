package com.polimi.childcare.server.handlers.entities.getters;

import com.polimi.childcare.shared.entities.Pediatra;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredPediatraRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListPediatraResponse;

import java.util.ArrayList;

public class FilteredPediatraRequestHandler extends FilteredRequestHandler<FilteredPediatraRequest, Pediatra>
{
    @Override
    public BaseResponse processRequest(FilteredPediatraRequest request)
    {
        if(request.getCount() < 0 || request.getPageNumber() < 0)
            return new BadRequestResponse();

        return new ListPediatraResponse(200, getFilteredResult(request, Pediatra.class, new ArrayList<>()));
    }
}
