package com.polimi.childcare.server.handlers.entities.special;

import com.polimi.childcare.server.helper.query.RegistroPresenzeQuery;
import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.RegistroPresenze;
import com.polimi.childcare.shared.networking.requests.special.SetPresenzaRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

import java.util.ArrayList;

public class SetPresenzaRequestHandler implements IRequestHandler<SetPresenzaRequest>
{

    @Override
    public BaseResponse processRequest(SetPresenzaRequest request)
    {
        ArrayList<RegistroPresenze> listPresenze = RegistroPresenzeQuery.getStatoPresenzeAtEpochSeconds(request.getUtcInstant(), request.getBambinoId());
        //Aggiornare stati Bambino su DB (dopo utc istant), ritornare ListPresenze con ultimo stato aggiornato, in caso di errore vuota

        return new BaseResponse(200);
    }
}
