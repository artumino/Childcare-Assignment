package com.polimi.childcare.server.handlers.entities.getters;

import com.polimi.childcare.shared.entities.Gita;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredGitaRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListGitaResponse;

import java.util.ArrayList;

public class FilteredGitaRequestHandler extends FilteredRequestHandler<FilteredGitaRequest, Gita>
{
    @Override
    public BaseResponse processRequest(FilteredGitaRequest request)
    {
        if(request.getCount() < 0 || request.getPageNumber() < 0)
            return new BadRequestResponse();

        return new ListGitaResponse(200, getFilteredResult(request, Gita.class, new ArrayList<>()));

    }
}
