package com.polimi.childcare.server.handlers.entities.special;

import com.polimi.childcare.server.helper.query.RegistroPresenzeQuery;
import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.RegistroPresenze;
import com.polimi.childcare.shared.networking.requests.special.SetPresenzaRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.utils.EntitiesHelper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class SetPresenzaRequestHandler implements IRequestHandler<SetPresenzaRequest>
{

    @Override
    public BaseResponse processRequest(SetPresenzaRequest request)
    {
        ArrayList<RegistroPresenze> listPresenze = RegistroPresenzeQuery.getStatoPresenzeAtEpochSeconds(request.getUtcInstant(), request.getBambinoId());
        //EntitiesHelper.presenzeChangerRecursive(listPresenze, request.getNuovoStato(), (LocalDateTime)request.getUtcInstant(), request.isUscita());
        //Aggiornare stati Bambino su DB (dopo utc istant), ritornare ListPresenze con ultimo stato aggiornato, in caso di errore vuota

        //TODO: secondo me mancano nella richiesta il NuovoStato e utcIstant deve essere convertito a LocalDateTime

        return new BaseResponse(200);
    }
}
