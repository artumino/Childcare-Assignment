package com.polimi.childcare.server.handlers.entities.getters;

import com.polimi.childcare.shared.dto.DTOUtils;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredBaseRequest;
import com.polimi.childcare.shared.entities.TransferableEntity;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.stream.Stream;

public class FilteredRequestHandler
{
    /**
     *Classe helper che gestisce le varie richieste e ritorna in risposta una lista con il risultato della stream
     * @param request Richiesta di Tipo T
     * @param itemClass Classe del Tipo IT
     * @param list Lista di Tipo IT
     * @param <T> Tipo della Richiesta
     * @param <IT> Tipo della Classe Legata alla Richiesta
     * @return Lista di Tipo IT
     */
    public static <T extends FilteredBaseRequest<IT>, IT extends TransferableEntity> List<IT> requestManager(T request, Class<IT> itemClass, List<IT> list)
    {
        if (request.getCount() == 0)
            DatabaseSession.getInstance().execute(session -> {
                Stream<IT> stream =  session.stream(itemClass);

                try {
                    DBHelper.filterAdd(stream, request.getOrderBy(), request.getFilters());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                CollectionUtils.addAll(list, stream.iterator());

                if(request.isDetailed())
                    DBHelper.recursiveIterableIntitialize(list);
                return true;
            });

        else
            DatabaseSession.getInstance().execute(session -> {
                Stream<IT> stream =  session.stream(itemClass);

                try {
                    DBHelper.filterAdd(stream, request.getOrderBy(), request.getFilters());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                CollectionUtils.addAll(list, stream.limit(request.getCount() * (request.getPageNumber() + 1)).skip(request.getCount() * request.getPageNumber()).iterator());

                if(request.isDetailed())
                    DBHelper.recursiveIterableIntitialize(list);
                return true;
            });

        
        //Trasforma i proxy
        DTOUtils.iterableToDTO(list, null);
        
        return list;
    }
}
