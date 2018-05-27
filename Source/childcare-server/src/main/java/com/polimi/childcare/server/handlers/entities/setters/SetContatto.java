package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.entities.Contatto;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

import java.util.Set;

public class SetContatto implements IRequestHandler<SetEntityRequest<Contatto>>
{
    @Override
    public BaseResponse processRequest(SetEntityRequest<Contatto> request)
    {
        final BaseResponse[] response = new BaseResponse[1];
        Throwable exception = DatabaseSession.getInstance().execute((session) -> {

            Contatto contattoget = session.getByID(Contatto.class, request.getEntity().getID(), true);
            Set<Bambino> bambiniset = contattoget.getBambini();

            if(request.isToDelete()) {
                for (Bambino b : bambiniset) {
                    contattoget.removeBambino(b);
                }
            }


            response[0] = SetGenericEntity.Setter(request, Contatto.class, session);

            return !(response[0] instanceof BadRequestResponse);
        });

        if(exception != null)
            return new BadRequestResponse.BadRequestResponseWithMessage(exception.getMessage());

        return response[0];
    }
}