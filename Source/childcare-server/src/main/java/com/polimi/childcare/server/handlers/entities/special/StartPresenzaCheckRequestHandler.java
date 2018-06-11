package com.polimi.childcare.server.handlers.entities.special;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.database.dao.implementations.RegistroPresenzeDaoImpl;
import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.entities.RegistroPresenze;
import com.polimi.childcare.shared.networking.requests.special.StartPresenzaCheckRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class StartPresenzaCheckRequestHandler implements IRequestHandler<StartPresenzaCheckRequest>
{
    @Override
    public BaseResponse processRequest(StartPresenzaCheckRequest request)
    {
        LocalDateTime lt = LocalDateTime.now();
        DatabaseSession.getInstance().execute( execution ->
        {
            RegistroPresenzeDaoImpl presenzeDao = new RegistroPresenzeDaoImpl(execution);
            List<RegistroPresenze> registroPresenze = presenzeDao.getLastPresenze();
            HashMap<Bambino, RegistroPresenze.StatoPresenza> statoPresenzaHashMap = new HashMap<>(registroPresenze.size());
            for(RegistroPresenze presenze : registroPresenze)
                if(presenze.getBambino() != null)
                    statoPresenzaHashMap.put(presenze.getBambino(), presenze.getStato());

            //Ottiene tutti i bambini presenti alla gita
            List<Bambino> bambinoList = execution.stream(Bambino.class).filter(b -> statoPresenzaHashMap.containsKey(b) && statoPresenzaHashMap.get(b) != RegistroPresenze.StatoPresenza.Assente).collect(Collectors.toList());

            for(Bambino bambino : bambinoList)
                presenzeDao.insert(new RegistroPresenze(RegistroPresenze.StatoPresenza.Disperso, lt.toLocalDate(), lt, (short)lt.getHour(), bambino, request.getGita()));

            return true;
        });
        return new BaseResponse(200);
    }
}
