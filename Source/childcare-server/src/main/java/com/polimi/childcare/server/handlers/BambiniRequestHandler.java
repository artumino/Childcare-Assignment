package com.polimi.childcare.server.handlers;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.networking.requests.BambiniRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListBambiniResponse;

import java.util.ArrayList;
import java.util.List;

public class BambiniRequestHandler implements IRequestHandler<BambiniRequest>
{
    @Override
    public BaseResponse processRequest(BambiniRequest request)
    {
        if(request.getCount() < 0 || request.getPageNumber() < 0)
            return new BadRequestResponse();
        List<Bambino> bambini = new ArrayList<>();
        if(request.getCount() == 0)
            DatabaseSession.getInstance().execute(session -> {
                bambini.addAll(session.query(Bambino.class).toList());
                return true;
            });
        else
            DatabaseSession.getInstance().execute(session -> {
                bambini.addAll(session.query(Bambino.class).limit(request.getCount()*(request.getPageNumber() + 1)).skip(request.getCount()*request.getPageNumber()).toList());
                return true;
            });

        ListBambiniResponse risposta = new ListBambiniResponse(200, bambini);

        return risposta;
    }
}
