package com.polimi.childcare.server.handlers.entities.special;

import com.polimi.childcare.server.helper.query.RegistroPresenzeQuery;
import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.RegistroPresenze;
import com.polimi.childcare.shared.networking.requests.special.SetPresenzaRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.utils.EntitiesHelper;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

public class SetPresenzaRequestHandler implements IRequestHandler<SetPresenzaRequest>
{

    @Override
    public BaseResponse processRequest(SetPresenzaRequest request)
    {
        ArrayList<RegistroPresenze> listPresenze = RegistroPresenzeQuery.getStatoPresenzeAtEpochSeconds(request.getUtcInstant(), request.getBambinoId());
        LocalDateTime lt = Instant.ofEpochMilli(request.getUtcInstant()).atZone(ZoneId.systemDefault()).toLocalDateTime();
        RegistroPresenze.StatoPresenza st = RegistroPresenze.StatoPresenza.Disperso;
        try {
            st = EntitiesHelper.presenzeChanger(listPresenze.get(0), lt, request.isUscita());
        } catch (Exception e) {
            e.printStackTrace();
        }

        EntitiesHelper.presenzeChangerRecursive(listPresenze, st, lt, request.isUscita());

        //Ho capito come l'avevo pensata :D

        //Aggiornare stati Bambino su DB (dopo utc istant), ritornare ListPresenze con ultimo stato aggiornato, in caso di errore vuota

        return new BaseResponse(200);
    }
}
