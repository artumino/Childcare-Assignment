package com.polimi.childcare.server.handlers;

import com.polimi.childcare.server.Helper.DBHelper;
import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.Fornitore;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredFornitoriRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListFornitoriResponse;
import org.jinq.orm.stream.JinqStream;

import java.util.ArrayList;
import java.util.List;

public class FilteredFornitoreRequestHandler implements IRequestHandler<FilteredFornitoriRequest>
{
    @Override
    public BaseResponse processRequest(FilteredFornitoriRequest request)
    {
        if(request.getCount() < 0 || request.getPageNumber() < 0)
            return new BadRequestResponse();

        return new ListFornitoriResponse(200, FilteredRequestHandler.requestManager(request, Fornitore.class, new ArrayList<Fornitore>()));
    }
}