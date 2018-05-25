package com.polimi.childcare.server.handlers.entities.getters;

import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.NumeroTelefono;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredNumeroTelefonoRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListNumeroTelefonoResponse;

import java.util.ArrayList;

public class FilteredNumeroTelefonoRequestHandler implements IRequestHandler<FilteredNumeroTelefonoRequest>
{
    @Override
    public BaseResponse processRequest(FilteredNumeroTelefonoRequest request)
    {
        if(request.getCount() < 0 || request.getPageNumber() < 0)
            return new BadRequestResponse();

        return new ListNumeroTelefonoResponse(200, FilteredRequestHandler.requestManager(request, NumeroTelefono.class, new ArrayList<>()));
    }
}