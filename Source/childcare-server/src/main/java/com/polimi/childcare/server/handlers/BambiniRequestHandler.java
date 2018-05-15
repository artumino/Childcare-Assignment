package com.polimi.childcare.server.handlers;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.networking.requests.BambiniRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListBambiniResponse;

import java.util.ArrayList;
import java.util.List;

public class BambiniRequestHandler implements IRequestHandler<BambiniRequest>
{
    @Override
    public BaseResponse processRequest(BambiniRequest request)
    {
        List<Bambino> bambini = new ArrayList<>();
        DatabaseSession.getInstance().execute(session -> {
            bambini.addAll(session.query(Bambino.class).toList());
            return true;
        });

        ListBambiniResponse risposta = new ListBambiniResponse(200, bambini);

        return risposta;


    }
}
