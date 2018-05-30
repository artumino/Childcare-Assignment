package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.shared.entities.Diagnosi;
import com.polimi.childcare.shared.entities.Pediatra;
import com.polimi.childcare.shared.entities.Persona;
import com.polimi.childcare.shared.entities.ReazioneAvversa;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

public class DiagnosiRequestHandlerSet extends GenericSetEntityRequestHandler<SetEntityRequest<Diagnosi>, Diagnosi>
{
    @Override
    protected Class<Diagnosi> getQueryClass() {
        return Diagnosi.class;
    }

    @Override
    protected void doPreSetChecks(DatabaseSession.DatabaseSessionInstance session, SetEntityRequest<Diagnosi> request, Diagnosi dbEntity)
    {
        if (dbEntity != null && request.getOldHashCode() == dbEntity.consistecyHashCode())
        {
            if (!request.isToDelete())
            {
                if(request.getEntity().getPersona() == null || request.getEntity().getReazioneAvversa() == null)
                    throw new RuntimeException("Persona e/o Reazione nulli!");

                DBHelper.updateManyToOne(request.getEntity().asDiagnosiPersonaRelation(), Persona.class, session);
                DBHelper.updateManyToOne(request.getEntity().asDiagnosiReazioneAvversaRelation(), ReazioneAvversa.class, session);
            }
        }
    }
}