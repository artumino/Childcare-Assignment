package com.polimi.childcare.server.handlers.entities.special;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.database.dao.DaoFactory;
import com.polimi.childcare.server.database.dao.ICommonDao;
import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.entities.Gruppo;
import com.polimi.childcare.shared.entities.PianoViaggi;
import com.polimi.childcare.shared.networking.requests.special.UpdateGruppiRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UpdateGruppiRequestHandler implements IRequestHandler<UpdateGruppiRequest>
{

    @Override
    public BaseResponse processRequest(UpdateGruppiRequest request)
    {
        Throwable exception;

        //In questo caso usando le lambda exception il try & catch fa il catch solo delle espressioni che vanno in errore nella lambda expression,
        //ciò che invece scatena eccezioni sul DB scatena un altro try&catch che ritorna come risultato di execute
        try {
            exception = DatabaseSession.getInstance().execute(execution -> {
                ICommonDao<Gruppo> gruppoDao = DaoFactory.getInstance().getDao(Gruppo.class, execution);

                List<Gruppo> dbGruppi = execution.stream(Gruppo.class).collect(Collectors.toList());
                List<Gruppo> newGruppi = request.getNewGruppi();

                List<Gruppo> removed = new ArrayList<>();
                List<Gruppo> inserted = new ArrayList<>();

                //Inseriti
                for (Gruppo gruppo : newGruppi)
                    if (!dbGruppi.contains(gruppo))
                        inserted.add(gruppo);


                //Rimossi
                for (Gruppo gruppo : dbGruppi)
                    if (!newGruppi.contains(gruppo))
                        removed.add(gruppo);

                //Aggiornati
                List<Gruppo> updated = new ArrayList<>(newGruppi);
                for (Gruppo gruppo : inserted)
                    updated.remove(gruppo);

                //Rimuovo i rimossi
                for (Gruppo gruppo : removed)
                    gruppoDao.delete(gruppo);

                //Aggiorno gli aggiornati
                for (Gruppo gruppo : updated)
                    gruppoDao.update(gruppo);

                //Inserisco gli inseriti
                for (Gruppo gruppo : inserted)
                    gruppoDao.insert(gruppo);

                return true;
            });
        }
        catch (Exception ex)
        {
            exception = ex;
        }

        if(exception != null)
            return new BadRequestResponse.BadRequestResponseWithMessage(exception.getMessage());

        return new BaseResponse(200);
    }
}
