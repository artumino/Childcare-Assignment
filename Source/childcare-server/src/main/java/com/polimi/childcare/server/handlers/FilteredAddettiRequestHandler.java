package com.polimi.childcare.server.handlers;

import com.polimi.childcare.server.Helper.DBHelper;
import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.dto.DTOUtils;
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
    public BaseResponse processRequest(FilteredAddettoRequest request) {
        if (request.getCount() < 0 || request.getPageNumber() < 0)
            return new BadRequestResponse();

        List<Addetto> addetti = new ArrayList<>();

        if (request.getCount() == 0)
            DatabaseSession.getInstance().execute(session -> {
                JinqStream query = session.query(Addetto.class);

                try {
                    DBHelper.filterAdd(query, request.getOrderBy(), request.getFilters());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                addetti.addAll(session.query(Addetto.class).toList());
                return true;
            });

        else
            DatabaseSession.getInstance().execute(session -> {
                JinqStream query = session.query(Addetto.class);

                try {
                    DBHelper.filterAdd(query, request.getOrderBy(), request.getFilters());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                addetti.addAll(session.query(Addetto.class).limit(request.getCount() * (request.getPageNumber() + 1)).skip(request.getCount() * request.getPageNumber()).toList());
                return true;
            });

        if(request.isDetailed())
            DBHelper.recursiveObjectInitialize(addetti);

        DTOUtils.iterableToDTO(addetti);
        ListAddettiResponse risposta = new ListAddettiResponse(200, addetti);

        return risposta;
    }
}