package com.polimi.childcare.server.handlers.entities.special;

import com.polimi.childcare.server.helper.query.RegistroPresenzeQuery;
import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.RegistroPresenze;
import com.polimi.childcare.shared.networking.requests.setters.SetPresenzaRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

import java.util.ArrayList;

public class SetPresenzaRequestHandler implements IRequestHandler<SetPresenzaRequest>
{
    @Override
    public BaseResponse processRequest(SetPresenzaRequest request)
    {
        ArrayList<RegistroPresenze> listPresenze = RegistroPresenzeQuery.getStatoPresenzeAtEpochSeconds(request.getUtcInstant(), request.getBambinoId());


        return new BaseResponse(200);
    }
}
