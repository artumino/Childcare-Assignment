package com.polimi.childcare.server.handlers;

import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.Pasto;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredPastoRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListPastiResponse;

import java.util.ArrayList;

public class FilteredPastiRequestHandler implements IRequestHandler<FilteredPastoRequest>
{
    @Override
    public BaseResponse processRequest(FilteredPastoRequest request)
    {
        if(request.getCount() < 0 || request.getPageNumber() < 0)
            return new BadRequestResponse();

        return new ListPastiResponse(200, FilteredRequestHandler.requestManager(request, Pasto.class, new ArrayList<Pasto>()));
    }
}