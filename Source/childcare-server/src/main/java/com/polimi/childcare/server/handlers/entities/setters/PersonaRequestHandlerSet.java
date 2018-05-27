package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.shared.entities.Persona;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

public class PersonaRequestHandlerSet extends GenericSetEntityRequestHandler<SetEntityRequest<Persona>, Persona>
{
    @Override
    public BaseResponse processRequest(SetEntityRequest<Persona> request)
    {
        final BaseResponse[] response = new BaseResponse[1];
        DatabaseSession.getInstance().execute(session -> {
            return !((response[0] = requestSet(request, Persona.class, session)) instanceof BadRequestResponse);
        });
        return response[0];
    }
}
