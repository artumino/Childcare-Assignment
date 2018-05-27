package com.polimi.childcare.server.handlers.entities.getters;

import com.polimi.childcare.shared.entities.QuantitaPasto;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredQuantitaPastoRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListQuantitaPastoResponse;

import java.util.ArrayList;

public class FilteredQuantitaPastoRequestHandler extends FilteredRequestHandler<FilteredQuantitaPastoRequest, QuantitaPasto>
{
    @Override
    public BaseResponse processRequest(FilteredQuantitaPastoRequest request)
    {
        if(request.getCount() < 0 || request.getPageNumber() < 0)
            return new BadRequestResponse();

        return new ListQuantitaPastoResponse(200, getFilteredResult(request, QuantitaPasto.class, new ArrayList<>()));
    }
}