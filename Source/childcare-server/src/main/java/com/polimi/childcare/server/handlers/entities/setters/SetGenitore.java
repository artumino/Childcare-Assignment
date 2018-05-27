package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.entities.Genitore;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

import java.util.Set;

public class SetGenitore implements IRequestHandler<SetEntityRequest<Genitore>>
{
    @Override
    public BaseResponse processRequest(SetEntityRequest<Genitore> request)
    {
        final BaseResponse[] response = new BaseResponse[1];
        Throwable exception = DatabaseSession.getInstance().execute(session -> {
            Genitore genitoreget = session.getByID(Genitore.class, request.getEntity().getID(), true);
            Set<Bambino> bambini = request.getEntity().getBambini();

            for (Bambino b : bambini)
            {
                if(b.getGenitori().size() == 1)
                    throw new RuntimeException("Operazione illegale, avrei dei bambini orfani!");
                b.removeGenitore(genitoreget);
                session.update(b);
            }


            return !((response[0] = SetGenericEntity.Setter(request, Genitore.class, session)) instanceof BadRequestResponse);
        });

        if(exception != null)
            return new BadRequestResponse.BadRequestResponseWithMessage(exception.getMessage());

        return response[0];
    }
}
