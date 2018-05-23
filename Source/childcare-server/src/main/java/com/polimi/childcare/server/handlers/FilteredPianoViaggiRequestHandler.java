package com.polimi.childcare.server.handlers;

import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.PianoViaggi;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredPianoViaggiRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListPianoViaggiResponse;

import java.util.ArrayList;

public class FilteredPianoViaggiRequestHandler implements IRequestHandler<FilteredPianoViaggiRequest>
{
    @Override
    public BaseResponse processRequest(FilteredPianoViaggiRequest request)
    {
        if(request.getCount() < 0 || request.getPageNumber() < 0)
            return new BadRequestResponse();

        return new ListPianoViaggiResponse(200, FilteredRequestHandler.requestManager(request, PianoViaggi.class, new ArrayList<PianoViaggi>()));
    }
}
