package com.polimi.childcare.server.handlers.entities.getters;

import com.polimi.childcare.shared.entities.Diagnosi;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredDiagnosiRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListDiagnosiResponse;

import java.util.ArrayList;

public class FilteredDiagnosiRequestHandler extends FilteredRequestHandler<FilteredDiagnosiRequest, Diagnosi>
{
    @Override
    public BaseResponse processRequest(FilteredDiagnosiRequest request)
    {
        if(request.getCount() < 0 || request.getPageNumber() < 0)
            return new BadRequestResponse();

        return new ListDiagnosiResponse(200, getFilteredResult(request, Diagnosi.class, new ArrayList<>()));
    }
}
