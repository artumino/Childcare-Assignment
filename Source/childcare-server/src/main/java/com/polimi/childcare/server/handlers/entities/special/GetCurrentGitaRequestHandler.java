package com.polimi.childcare.server.handlers.entities.special;

import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.networking.requests.special.GetCurrentGitaRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListGitaResponse;

import java.util.ArrayList;

public class GetCurrentGitaRequestHandler implements IRequestHandler<GetCurrentGitaRequest>
{
    @Override
    public BaseResponse processRequest(GetCurrentGitaRequest request)
    {
        //TODO: Implementare
        return new ListGitaResponse(200, new ArrayList<>());
    }
}
