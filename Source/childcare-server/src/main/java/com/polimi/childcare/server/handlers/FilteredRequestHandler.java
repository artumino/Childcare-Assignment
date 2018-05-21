package com.polimi.childcare.server.handlers;

import com.polimi.childcare.server.Helper.DBHelper;
import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.networking.requests.BaseRequest;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredBaseRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import org.jinq.orm.stream.JinqStream;

import java.util.AbstractList;
import java.util.ArrayList;

public class FilteredRequestHandler
{
    public static <T extends BaseResponse, IT extends AbstractList, R extends FilteredBaseRequest> T requestManager(R request, T responselist, Class<? extends BaseRequest> requestClass, IT list)
    {
        if (request.getCount() < 0 || request.getPageNumber() < 0)
            return new BadRequestResponse();    /**FIXME: Mi serve aiuto con i Generics **/

        list = new ArrayList<>();

        if (request.getCount() == 0)
            DatabaseSession.getInstance().execute(session -> {
                JinqStream query = session.query(requestClass);

                try {
                    DBHelper.filterAdd(query, request.getOrderBy(), request.getFilters());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                list.addAll(session.query(requestClass).toList());
                return true;
            });

        else
            DatabaseSession.getInstance().execute(session -> {
                JinqStream query = session.query(requestClass);

                try {
                    DBHelper.filterAdd(query, request.getOrderBy(), request.getFilters());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                list.addAll(session.query(requestClass).limit(request.getCount() * (request.getPageNumber() + 1)).skip(request.getCount() * request.getPageNumber()).toList());
                return true;
            });

        if(request.isDetailed())
            DBHelper.recursiveObjectInitialize(list);

        responselist = new T(200, list);

        return responselist;
    }
}
