package com.polimi.childcare.server.handlers.entities.special;

import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.networking.requests.special.GetPersoneWithDisagnosiRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListPersoneResponse;

import java.util.ArrayList;

/**
 * Ritorna una lista di persone non dettagliata ma con la lista delle Diagnosi e ReazioniAvverse inizializzata.
 */
public class GetPersoneConDiagnosiHandler implements IRequestHandler<GetPersoneWithDisagnosiRequest>
{
    @Override
    public BaseResponse processRequest(GetPersoneWithDisagnosiRequest request) {
        //TODO: Implementare
        return new ListPersoneResponse(200, new ArrayList<>());
    }
}
