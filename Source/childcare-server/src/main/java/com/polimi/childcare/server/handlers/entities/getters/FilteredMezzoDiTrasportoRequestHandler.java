package com.polimi.childcare.server.handlers.entities.getters;

import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.MezzoDiTrasporto;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredMezzoDiTrasportoRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListMezzoDiTrasportoResponse;

import java.util.ArrayList;

public class FilteredMezzoDiTrasportoRequestHandler implements IRequestHandler<FilteredMezzoDiTrasportoRequest>
{
    @Override
    public BaseResponse processRequest(FilteredMezzoDiTrasportoRequest request)
    {
        if(request.getCount() < 0 || request.getPageNumber() < 0)
            return new BadRequestResponse();

        return new ListMezzoDiTrasportoResponse(200, FilteredRequestHandler.requestManager(request, MezzoDiTrasporto.class, new ArrayList<MezzoDiTrasporto>()));
    }
}
