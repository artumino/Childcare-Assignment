package com.polimi.childcare.server.handlers.entities.special;

import com.polimi.childcare.server.handlers.entities.getters.FilteredRequestHandler;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.shared.entities.Persona;
import com.polimi.childcare.shared.networking.requests.special.GetPersoneWithDisagnosiRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListPersoneResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Ritorna una lista di persone non dettagliata ma con la lista delle Diagnosi e ReazioniAvverse inizializzata.
 */
public class GetPersoneConDiagnosiHandler extends FilteredRequestHandler<GetPersoneWithDisagnosiRequest ,Persona>
{
    @Override
    public BaseResponse processRequest(GetPersoneWithDisagnosiRequest request)
    {
        List<Persona> persone = getFilteredResult(request, Persona.class, new ArrayList<>());
        persone.forEach(persona -> DBHelper.objectInitialize(persona.getDiagnosi()));
        return new ListPersoneResponse(200, persone);
    }
}
