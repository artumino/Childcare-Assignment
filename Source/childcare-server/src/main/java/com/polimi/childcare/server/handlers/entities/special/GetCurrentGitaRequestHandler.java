package com.polimi.childcare.server.handlers.entities.special;

import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.Gita;
import com.polimi.childcare.shared.networking.requests.special.GetCurrentGitaRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListGitaResponse;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;

public class GetCurrentGitaRequestHandler implements IRequestHandler<GetCurrentGitaRequest>
{
    @Override
    public BaseResponse processRequest(GetCurrentGitaRequest request)
    {
        if(request.getUtcInstantEpochSeconds()  < 0)
            return new BadRequestResponse();

        ArrayList<Gita> gita = new ArrayList<>();
        //gita.add(new ArrayList<>().stream().filter(p -> > request.getUtcInstantEpochSeconds()))
        return new ListGitaResponse(200, new ArrayList<>());
    }
}
