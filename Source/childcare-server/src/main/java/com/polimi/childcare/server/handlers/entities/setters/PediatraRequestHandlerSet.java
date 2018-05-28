package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.entities.Pediatra;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

import java.util.Set;

public class PediatraRequestHandlerSet extends GenericSetEntityRequestHandler<SetEntityRequest<Pediatra>, Pediatra>
{
    @Override
    protected Class<Pediatra> getQueryClass() {
        return Pediatra.class;
    }

    @Override
    protected void doPreSetChecks(DatabaseSession.DatabaseSessionInstance session, SetEntityRequest<Pediatra> request, Pediatra dbEntity) {
        //FIXME: Fixarlo
        Set<Bambino> bambiniset = dbEntity.getBambini();

        if(request.isToDelete()) {
            for (Bambino b : bambiniset) {
                dbEntity.removeBambino(b);
            }
        }
    }
}