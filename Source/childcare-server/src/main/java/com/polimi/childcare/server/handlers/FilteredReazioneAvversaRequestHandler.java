package com.polimi.childcare.server.handlers;

import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.ReazioneAvversa;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredReazioneAvversaRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListReazioneAvversaResponse;

import java.util.ArrayList;

public class FilteredReazioneAvversaRequestHandler implements IRequestHandler<FilteredReazioneAvversaRequest>
{
    @Override
    public BaseResponse processRequest(FilteredReazioneAvversaRequest request)
    {
        if(request.getCount() < 0 || request.getPageNumber() < 0)
            return new BadRequestResponse();

        return new ListReazioneAvversaResponse(200, FilteredRequestHandler.requestManager(request, ReazioneAvversa.class, new ArrayList<ReazioneAvversa>()));
    }
}
