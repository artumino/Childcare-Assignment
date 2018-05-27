package com.polimi.childcare.server.handlers.entities.getters;

import com.polimi.childcare.shared.entities.ReazioneAvversa;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredReazioneAvversaRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListReazioneAvversaResponse;

import java.util.ArrayList;

public class FilteredReazioneAvversaRequestHandler extends FilteredRequestHandler<FilteredReazioneAvversaRequest, ReazioneAvversa>
{
    @Override
    public BaseResponse processRequest(FilteredReazioneAvversaRequest request)
    {
        if(request.getCount() < 0 || request.getPageNumber() < 0)
            return new BadRequestResponse();

        return new ListReazioneAvversaResponse(200, getFilteredResult(request, ReazioneAvversa.class, new ArrayList<ReazioneAvversa>()));
    }
}
