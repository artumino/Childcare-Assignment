package com.polimi.childcare.server.handlers.entities.special;

import com.polimi.childcare.server.handlers.entities.getters.FilteredRequestHandler;
import com.polimi.childcare.shared.entities.Contatto;
import com.polimi.childcare.shared.entities.Pediatra;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredContattoOnlyRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListContattoResponse;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;

public class FilteredContattoOnlyRequestHandler extends FilteredRequestHandler<FilteredContattoOnlyRequest, Contatto>
{
    @Override
    public BaseResponse processRequest(FilteredContattoOnlyRequest request)
    {
        if(request.getCount() < 0 || request.getPageNumber() < 0)
            return new BadRequestResponse();

        ArrayList<Contatto> contatti = new ArrayList<>();
        CollectionUtils.addAll(contatti, getFilteredResult(request, Contatto.class, new ArrayList<>()).stream().filter(p -> !(p instanceof Pediatra)).iterator());
        return new ListContattoResponse(200, contatti);
    }
}