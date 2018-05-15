package com.polimi.childcare.server.handlers;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredBambiniRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListBambiniResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FilteredBambiniRequestHandler implements IRequestHandler<FilteredBambiniRequest>
{
    @Override
    public BaseResponse processRequest(FilteredBambiniRequest request)
    {
        ArrayList<Bambino> rs = new ArrayList<Bambino>();
        ListBambiniResponse risposta = new ListBambiniResponse(200, rs);

        /*switch(request.getFilters())
        {
            case "Nome":
                break;
                                                        //Qua chiedere a Jacopo per HashMap
            default:
                break;
        }

        DatabaseSession.getInstance().execute(session -> {
            List<Bambino> bambini =  session.query(Bambino.class).where((bambino) ->  bambino.getNome().contains("Ric") ).toList();
            return true;
        });*/

        return risposta;
    }
}
