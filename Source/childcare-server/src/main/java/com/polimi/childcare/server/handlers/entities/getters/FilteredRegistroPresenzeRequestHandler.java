package com.polimi.childcare.server.handlers.entities.getters;

import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.RegistroPresenze;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredRegistroPresenzeRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListRegistroPresenzeResponse;

import java.util.ArrayList;

public class FilteredRegistroPresenzeRequestHandler implements IRequestHandler<FilteredRegistroPresenzeRequest>
{
    @Override
    public BaseResponse processRequest(FilteredRegistroPresenzeRequest request)
    {
        if(request.getCount() < 0 || request.getPageNumber() < 0)
            return new BadRequestResponse();

        return new ListRegistroPresenzeResponse(200, FilteredRequestHandler.requestManager(request, RegistroPresenze.class, new ArrayList<RegistroPresenze>()));
    }
}
