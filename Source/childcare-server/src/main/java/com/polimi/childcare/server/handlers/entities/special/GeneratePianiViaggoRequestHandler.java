package com.polimi.childcare.server.handlers.entities.special;

import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.networking.requests.special.GeneratePianiViaggioRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

/**
 * Controlla che i gruppi siano validi, inserisce i gruppi nel DB e crea i PianiViaggio per legare i gruppi a Gita e MezzoDiTrasporto grazie alla mappa nella richiesta.
 * Ritorna una BaseResponse con il risultato.
 */
public class GeneratePianiViaggoRequestHandler implements IRequestHandler<GeneratePianiViaggioRequest>
{
    @Override
    public BaseResponse processRequest(GeneratePianiViaggioRequest request) {
        //TODO: Implementare
        return new BaseResponse(300); //KO
    }
}
