package com.polimi.childcare.server.handlers.entities.special;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.helper.query.RegistroPresenzeQuery;
import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.RegistroPresenze;
import com.polimi.childcare.shared.networking.requests.special.SetPresenzaRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.utils.EntitiesHelper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class SetPresenzaRequestHandler implements IRequestHandler<SetPresenzaRequest>
{

    @Override
    public BaseResponse processRequest(SetPresenzaRequest request)
    {
        ArrayList<RegistroPresenze> listPresenze = RegistroPresenzeQuery.getStatoPresenzeAtEpochSeconds(request.getUtcInstant(), request.getBambinoId());
        LocalDateTime lt = Instant.ofEpochMilli(request.getUtcInstant()).atZone(ZoneId.systemDefault()).toLocalDateTime();
        RegistroPresenze.StatoPresenza st = RegistroPresenze.StatoPresenza.Presente;
        try
        {
            if(listPresenze.size() != 0)
                st = EntitiesHelper.presenzeChanger(listPresenze.get(0), lt, request.isUscita());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        List<RegistroPresenze> removed = new ArrayList<>();
        EntitiesHelper.presenzeChangerRecursive(listPresenze, st, lt, request.isUscita(), removed);

        if(removed.size() != 0)
        {
            for (RegistroPresenze r : removed)
                DatabaseSession.getInstance().delete(r);
        }

        if(listPresenze.size() != 0)
        {
            for (RegistroPresenze au : listPresenze)
                DatabaseSession.getInstance().insertOrUpdate(au);
        }

        return new BaseResponse(200);
    }
}
