package com.polimi.childcare.server.handlers;

import com.polimi.childcare.server.Helper.DBHelper;
import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredBaseRequest;
import org.jinq.orm.stream.JinqStream;

import java.util.Collection;
import java.util.List;

public class FilteredRequestHandler
{
    public static <T extends FilteredBaseRequest, IT> List<IT> requestManager(T request, Class<?> requestClass, List<IT> list)
    {
        if (request.getCount() == 0)
            DatabaseSession.getInstance().execute(session -> {
                JinqStream query = session.query(requestClass);

                try {
                    DBHelper.filterAdd(query, request.getOrderBy(), request.getFilters());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                list.addAll((Collection<? extends IT>) session.query(requestClass).toList());
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

                list.addAll((Collection<? extends IT>) session.query(requestClass).limit(request.getCount() * (request.getPageNumber() + 1)).skip(request.getCount() * request.getPageNumber()).toList());
                return true;
            });

        if(request.isDetailed())
            DBHelper.recursiveObjectInitialize(list);

        return list;
    }
}
