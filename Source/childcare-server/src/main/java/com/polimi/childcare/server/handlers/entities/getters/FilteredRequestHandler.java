package com.polimi.childcare.server.handlers.entities.getters;

import com.polimi.childcare.shared.dto.DTOUtils;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredBaseRequest;
import com.polimi.childcare.shared.entities.TransferableEntity;
import org.jinq.orm.stream.JinqStream;

import java.util.Collection;
import java.util.List;

public class FilteredRequestHandler
{
    /**
     *Classe helper che gestisce le varie richieste e ritorna in risposta una lista con il risultato della query
     * @param request Richiesta di Tipo T
     * @param requestClass Classe del Tipo IT
     * @param list Lista di Tipo IT
     * @param <T> Tipo della Richiesta
     * @param <IT> Tipo della Classe Legata alla Richiesta
     * @return Lista di Tipo IT
     */
    public static <T extends FilteredBaseRequest, IT extends TransferableEntity> List<IT> requestManager(T request, Class<?> requestClass, List<IT> list)
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
        
        //Trasforma i proxy
        DTOUtils.iterableToDTO(list);
        
        return list;
    }
}