package com.polimi.childcare.server.handlers.entities.special;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.networking.IRequestHandler;
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
        List<Gruppo> g = new ArrayList<>();
        List<PianoViaggi> list = new ArrayList<>();

        Gruppo gbuff;
        MezzoDiTrasporto mbuff;

        Iterator it = mappaGruppoToMezzoDiTrasporto.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry pair = (Map.Entry)it.next();

            gbuff = (Gruppo) pair.getKey();
            mbuff = (MezzoDiTrasporto) pair.getValue();

            //Controllo che sia valido il gruppo
            if(gbuff.getSorvergliante() == null)
                return new BadRequestResponse();


            if(gbuff != null || mbuff !=  null)
            {
                if(gbuff.getBambini().size() > mbuff.getCapienza())
                    return new BadRequestResponse();

                else {
                    g.add(gbuff);
                    list.add(new PianoViaggi(request.getGita(), gbuff, mbuff));
                }
            }

            else
                return new BadRequestResponse();

            it.remove();
        }

        DatabaseSession.getInstance().insertAll(g);
        DatabaseSession.getInstance().insertAll(list);

        return new BaseResponse(300);
    }
}
