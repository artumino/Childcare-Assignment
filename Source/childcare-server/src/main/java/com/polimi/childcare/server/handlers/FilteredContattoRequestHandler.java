package com.polimi.childcare.server.handlers;

import com.polimi.childcare.server.Helper.DBHelper;
import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.entities.Contatto;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredContattoRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListContattoResponse;
import org.jinq.orm.stream.JinqStream;

import java.util.ArrayList;
import java.util.List;

public class FilteredContattoRequestHandler implements IRequestHandler<FilteredContattoRequest>
{
    @Override
    public BaseResponse processRequest(FilteredContattoRequest request)
    {
        if(request.getCount() < 0 || request.getPageNumber() < 0)
            return new BadRequestResponse();
        List<Contatto> contatti = new ArrayList<>();
        if(request.getCount() == 0)
            DatabaseSession.getInstance().execute(session -> {
                JinqStream query = session.query(Contatto.class);

                try {
                    DBHelper.filterAdd(query, request.getOrderBy(), request.getFilters());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                contatti.addAll(session.query(Contatto.class).toList());
                return true;
            });
        else
            DatabaseSession.getInstance().execute(session -> {
                JinqStream query = session.query(Contatto.class);

                try {
                    DBHelper.filterAdd(query, request.getOrderBy(), request.getFilters());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                contatti.addAll(session.query(Contatto.class).limit(request.getCount()*(request.getPageNumber() + 1)).skip(request.getCount()*request.getPageNumber()).toList());
                return true;
            });


        if(request.isDetailed())
            DBHelper.recursiveObjectInitialize(contatti);

        ListContattoResponse risposta = new ListContattoResponse(200, contatti);

        return risposta;
    }
}