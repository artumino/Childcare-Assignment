package com.polimi.childcare.server.handlers.entities.getters;

import com.polimi.childcare.shared.entities.Menu;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredMenuRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListMenuResponse;

import java.util.ArrayList;

public class FilteredMenuRequestHandler extends FilteredRequestHandler<FilteredMenuRequest, Menu>
{
    @Override
    public BaseResponse processRequest(FilteredMenuRequest request)
    {
        if(request.getCount() < 0 || request.getPageNumber() < 0)
            return new BadRequestResponse();

        return new ListMenuResponse(200, getFilteredResult(request, Menu.class, new ArrayList<>()));
    }
}
