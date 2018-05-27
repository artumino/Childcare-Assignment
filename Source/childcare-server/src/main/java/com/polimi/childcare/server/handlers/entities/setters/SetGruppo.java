package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.entities.Gruppo;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

import java.util.Set;

public class SetGruppo implements IRequestHandler<SetEntityRequest<Gruppo>>
{
    @Override
    public BaseResponse processRequest(SetEntityRequest<Gruppo> request)
    {
        final BaseResponse[] response = new BaseResponse[1];
        Throwable exception = DatabaseSession.getInstance().execute(session -> {
            Gruppo gruppoget = session.getByID(Gruppo.class, request.getEntity().getID(), true);
            Set<Bambino> bambini = gruppoget.getBambini();

            if(request.isToDelete()) {
                for (Bambino b : bambini) {
                    b.setGruppo(null);
                    session.update(b);
                }
            }

            return !((response[0] = SetGenericEntity.Setter(request, Gruppo.class, session)) instanceof BadRequestResponse);
        });

        if(exception != null)
            return new BadRequestResponse.BadRequestResponseWithMessage(exception.getMessage());

        return response[0];
    }
}
