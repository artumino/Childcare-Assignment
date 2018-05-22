package com.polimi.childcare.server.handlers;

import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.Gita;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredGitaRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListGitaResponse;

import java.util.ArrayList;

public class FilteredGitaRequestHandler implements IRequestHandler<FilteredGitaRequest>
{
    @Override
    public BaseResponse processRequest(FilteredGitaRequest request)
    {
        if(request.getCount() < 0 || request.getPageNumber() < 0)
            return new BadRequestResponse();

        return new ListGitaResponse(200, FilteredRequestHandler.requestManager(request, Gita.class, new ArrayList<Gita>()));

    }
}
