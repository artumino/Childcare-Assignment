package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.Persona;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

public class PersonaRequestHandlerSet extends GenericSetEntityRequestHandler<SetEntityRequest<Persona>, Persona>
{
    @Override
    protected Class<Persona> getQueryClass() {
        return Persona.class;
    }

    @Override
    protected void doPreSetChecks(DatabaseSession.DatabaseSessionInstance session, SetEntityRequest<Persona> request, Persona dbEntity) {
        //TODO: Da fare
    }
}
