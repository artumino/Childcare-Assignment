package com.polimi.childcare.server.handlers;

import com.polimi.childcare.server.Helper.DBHelper;
import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.Addetto;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredAddettoRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListAddettiResponse;
import org.jinq.orm.stream.JinqStream;

import java.util.ArrayList;
import java.util.List;

public class FilteredAddettiRequestHandler implements IRequestHandler<FilteredAddettoRequest>
{
    @Override
    public BaseResponse processRequest(FilteredAddettoRequest request)
    {
        if(request.getCount() < 0 || request.getPageNumber() < 0)
            return new BadRequestResponse();

        return new ListAddettiResponse(200, FilteredRequestHandler.requestManager(request, Addetto.class, new ArrayList<Addetto>()));
    }
}