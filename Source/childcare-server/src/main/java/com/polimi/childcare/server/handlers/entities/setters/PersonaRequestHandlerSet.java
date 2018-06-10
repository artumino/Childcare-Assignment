package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.Addetto;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.entities.Genitore;
import com.polimi.childcare.shared.networking.requests.setters.SetAddettoRequest;
import com.polimi.childcare.shared.networking.requests.setters.SetBambinoRequest;
import com.polimi.childcare.shared.networking.requests.setters.SetGenitoreRequest;
import com.polimi.childcare.shared.networking.requests.setters.SetPersonaRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

public class PersonaRequestHandlerSet implements IRequestHandler<SetPersonaRequest>
{
    //Reindirizza semplicemente le richieste ai vari tipi specifici
    @Override
    public BaseResponse processRequest(SetPersonaRequest request)
    {
        if(request.getEntity() != null)
        {
            if(request.getEntity() instanceof Bambino)
                return (new BambinoRequestHandlerSet()).processRequest(new SetBambinoRequest((Bambino)request.getEntity(), request.isToDelete(), request.getOldHashCode()));
            else if(request.getEntity() instanceof Genitore)
                return (new GenitoreRequestHandlerSet()).processRequest(new SetGenitoreRequest((Genitore)request.getEntity(), request.isToDelete(), request.getOldHashCode()));
            else if(request.getEntity() instanceof Addetto)
                return (new AddettoRequestHandlerSet()).processRequest(new SetAddettoRequest((Addetto)request.getEntity(), request.isToDelete(), request.getOldHashCode()));
        }

        return new BadRequestResponse();
    }
}
