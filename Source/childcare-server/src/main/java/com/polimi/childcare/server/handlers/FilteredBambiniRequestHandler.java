package com.polimi.childcare.server.handlers;

import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredBambiniRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListBambiniResponse;

import java.util.ArrayList;

public class FilteredBambiniRequestHandler implements IRequestHandler<FilteredBambiniRequest>
{
    @Override
    public BaseResponse processRequest(FilteredBambiniRequest request)
    {
        if(request.getCount() < 0 || request.getPageNumber() < 0)
            return new BadRequestResponse();

        return new ListBambiniResponse(200, FilteredRequestHandler.requestManager(request, Bambino.class, new ArrayList<Bambino>()));
    }
}
