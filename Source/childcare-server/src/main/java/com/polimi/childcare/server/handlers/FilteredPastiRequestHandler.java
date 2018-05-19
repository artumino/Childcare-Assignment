package com.polimi.childcare.server.handlers;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.Pasto;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredPastoRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListPastiResponse;

import java.util.ArrayList;
import java.util.List;

public class FilteredPastiRequestHandler implements IRequestHandler<FilteredPastoRequest>
{
    @Override
    public BaseResponse processRequest(FilteredPastoRequest request)
    {
        if(request.getCount() < 0 || request.getPageNumber() < 0)
            return new BadRequestResponse();
        List<Pasto> pasti = new ArrayList<>();
        if(request.getCount() == 0)
            DatabaseSession.getInstance().execute(session -> {
                pasti.addAll(session.query(Pasto.class).toList());
                return true;
            });
        else
            DatabaseSession.getInstance().execute(session -> {
                pasti.addAll(session.query(Pasto.class).limit(request.getCount()*(request.getPageNumber() + 1)).skip(request.getCount()*request.getPageNumber()).toList());
                return true;
            });

        ListPastiResponse risposta = new ListPastiResponse(200, pasti);

        return risposta;


    }
}