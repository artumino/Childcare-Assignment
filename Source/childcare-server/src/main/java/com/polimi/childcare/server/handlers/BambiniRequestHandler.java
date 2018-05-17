package com.polimi.childcare.server.handlers;

import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredBambiniRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListBambiniResponse;

import java.util.ArrayList;

public class BambiniRequestHandler implements IRequestHandler<FilteredBambiniRequest>
{
    @Override
    public BaseResponse processRequest(FilteredBambiniRequest request)
    {
        ListBambiniResponse risposta = new ListBambiniResponse(200, new ArrayList<Bambino>());

        return risposta;
    }
}
