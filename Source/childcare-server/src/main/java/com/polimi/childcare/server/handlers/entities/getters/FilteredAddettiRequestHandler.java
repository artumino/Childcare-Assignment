package com.polimi.childcare.server.handlers.entities.getters;

import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.Addetto;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredBaseRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListAddettiResponse;

import java.util.ArrayList;

public class FilteredAddettiRequestHandler implements IRequestHandler<FilteredBaseRequest<Addetto>>
{
    @Override
    public BaseResponse processRequest(FilteredBaseRequest<Addetto> request)
    {
        if(request.getCount() < 0 || request.getPageNumber() < 0)
            return new BadRequestResponse();

        return new ListAddettiResponse(200, FilteredRequestHandler.requestManager(request, Addetto.class, new ArrayList<Addetto>()));
    }
}