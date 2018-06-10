package com.polimi.childcare.server.handlers.entities.special;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.database.dao.implementations.PianoViaggiDaoImpl;
import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.Gita;
import com.polimi.childcare.shared.entities.Gruppo;
import com.polimi.childcare.shared.entities.MezzoDiTrasporto;
import com.polimi.childcare.shared.entities.PianoViaggi;
import com.polimi.childcare.shared.networking.requests.special.GeneratePianiViaggioRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

import java.util.*;

/**
 * Controlla che i gruppi siano validi, inserisce i gruppi nel DB e crea i PianiViaggio per legare i gruppi a Gita e MezzoDiTrasporto grazie alla mappa nella richiesta.
 * Ritorna una BaseResponse con il risultato.
 */
public class GeneratePianiViaggoRequestHandler implements IRequestHandler<GeneratePianiViaggioRequest>
{
    @Override
    public BaseResponse processRequest(GeneratePianiViaggioRequest request) {
        if(request == null)
            return new BadRequestResponse();

        if(request.getGita() == null)
            return new BadRequestResponse();

        HashMap<Gruppo, MezzoDiTrasporto> mappaGruppoToMezzoDiTrasporto = request.getMappaGruppoToMezzoDiTrasporto();
        HashMap<MezzoDiTrasporto, Integer> mappaCapienzaResidua = new HashMap<>(mappaGruppoToMezzoDiTrasporto.size());
        List<Gruppo> g = new ArrayList<>();
        List<PianoViaggi> list = new ArrayList<>();

        Gruppo gbuff;
        MezzoDiTrasporto mbuff;

        Iterator it = mappaGruppoToMezzoDiTrasporto.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry<Gruppo,MezzoDiTrasporto> pair = (Map.Entry<Gruppo,MezzoDiTrasporto>)it.next();

            gbuff = pair.getKey();
            gbuff = DatabaseSession.getInstance().getByID(Gruppo.class, gbuff.getID(), true);
            mbuff = pair.getValue();

            if(gbuff == null)
                return new BadRequestResponse.BadRequestResponseWithMessage("Un gruppo è stato cancellato durante le modifiche, aggiornare l'interfaccia e rieffettuare le modifiche");


            //Se è la prima volta trovo questo autobus, tengo conto di quanta capienza ho
            if(mbuff != null && !mappaCapienzaResidua.containsKey(mbuff))
                mappaCapienzaResidua.put(mbuff, mbuff.getCapienza());

            //Controllo che sia valido il gruppo
            if(gbuff.getSorvergliante() == null)
                return new BadRequestResponse.BadRequestResponseWithMessage("Un gruppo è senza sorvegliante");


            if(mbuff != null)
            {
                mappaCapienzaResidua.put(mbuff, mappaCapienzaResidua.get(mbuff) - (gbuff.getBambini().size() + 1));
                if( mappaCapienzaResidua.get(mbuff) < 0)
                    return new BadRequestResponse.BadRequestResponseWithMessage("Non ci sono abbastanza posti sull'autobus " + mbuff.getTarga() + " per accomodare tutti i bambini ed istruttori!");
                else
                {
                    g.add(gbuff);
                    list.add(new PianoViaggi(request.getGita(), gbuff, mbuff));
                }
            }
            else
                list.add(new PianoViaggi(request.getGita(), gbuff, null));


            it.remove();
        }

        //DatabaseSession.getInstance().insertAll(g);


        DatabaseSession.getInstance().execute(execution -> {
            Gita dbGita = execution.getByID(Gita.class, request.getGita().getID());
            PianoViaggiDaoImpl dao = new PianoViaggiDaoImpl(execution);
            dao.deletePianiViaggioByGita(dbGita);

            for(PianoViaggi pianoViaggi : list)
                dao.insert(pianoViaggi);

            return true;
        });

        return new BaseResponse(200);
    }
}
