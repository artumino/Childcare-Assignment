package com.polimi.childcare.server.handlers;

import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.QuantitaPasto;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredQuantitaPastoRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListQuantitaPastoResponse;

import java.util.ArrayList;

public class FilteredQuantitaPastoRequestHandler implements IRequestHandler<FilteredQuantitaPastoRequest>
{
    @Override
    public BaseResponse processRequest(FilteredQuantitaPastoRequest request)
    {
        if(request.getCount() < 0 || request.getPageNumber() < 0)
            return new BadRequestResponse();

        return new ListQuantitaPastoResponse(200, FilteredRequestHandler.requestManager(request, QuantitaPasto.class, new ArrayList<QuantitaPasto>()));
    }
}