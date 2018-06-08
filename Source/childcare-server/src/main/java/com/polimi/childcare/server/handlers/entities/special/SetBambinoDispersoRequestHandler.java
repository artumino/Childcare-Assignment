package com.polimi.childcare.server.handlers.entities.special;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.entities.Gita;
import com.polimi.childcare.shared.entities.RegistroPresenze;
import com.polimi.childcare.shared.networking.requests.special.SetPresenzaRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class SetBambinoDispersoRequestHandler implements IRequestHandler<SetPresenzaRequest>
{
    @Override
    public BaseResponse processRequest(SetPresenzaRequest request)
    {
        LocalDateTime lt = Instant.ofEpochMilli(request.getUtcInstant()).atZone(ZoneId.systemDefault()).toLocalDateTime();
        Bambino b = DatabaseSession.getInstance().getByID(Bambino.class, request.getBambinoId());
        Gita g = DatabaseSession.getInstance().getByID(Gita.class, request.getGitaId());
        DatabaseSession.getInstance().insert(new RegistroPresenze(RegistroPresenze.StatoPresenza.Disperso, lt.toLocalDate(), lt, (short)lt.getHour(), b, g));
        return new BaseResponse(100);
    }
}
