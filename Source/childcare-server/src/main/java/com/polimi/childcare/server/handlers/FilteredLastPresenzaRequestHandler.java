package com.polimi.childcare.server.handlers;

import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.dto.DTOUtils;
import com.polimi.childcare.shared.entities.RegistroPresenze;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredLastPresenzaRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListRegistroPresenzeResponse;
import org.jinq.orm.stream.JinqStream;

import java.util.ArrayList;
import java.util.List;

public class FilteredLastPresenzaRequestHandler implements IRequestHandler<FilteredLastPresenzaRequest>
{
    @Override
    public BaseResponse processRequest(FilteredLastPresenzaRequest request)
    {
        if(request.getCount() < 0 || request.getPageNumber() < 0)
            return new BadRequestResponse();

        List<RegistroPresenze> list = new ArrayList<>();

        if (request.getCount() == 0)
            DatabaseSession.getInstance().execute(session -> {
                JinqStream query = session.query(RegistroPresenze.class);

                try {
                    //DBHelper.filterAdd(query, request.getOrderBy(), query.where("TimeStamp", ));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                list.addAll(session.query(RegistroPresenze.class).toList());
                return true;
            });

        else
            DatabaseSession.getInstance().execute(session -> {
                JinqStream query = session.query(RegistroPresenze.class);

                try {
                    DBHelper.filterAdd(query, request.getOrderBy(), request.getFilters());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                list.addAll( session.query(RegistroPresenze.class).limit(request.getCount() * (request.getPageNumber() + 1)).skip(request.getCount() * request.getPageNumber()).toList());
                return true;
            });

        if(request.isDetailed())
            DBHelper.recursiveObjectInitialize(list);

        //Trasforma i proxy
        DTOUtils.iterableToDTO(list);

        return new ListRegistroPresenzeResponse(200, list);
    }
}
