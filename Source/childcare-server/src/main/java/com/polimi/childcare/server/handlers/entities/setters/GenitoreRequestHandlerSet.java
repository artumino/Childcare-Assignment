package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.entities.Genitore;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

import java.util.Set;

public class GenitoreRequestHandlerSet extends GenericSetEntityRequestHandler<SetEntityRequest<Genitore>, Genitore>
{
    @Override
    protected Class<Genitore> getQueryClass() {
        return Genitore.class;
    }

    @Override
    protected void doPreSetChecks(DatabaseSession.DatabaseSessionInstance session, SetEntityRequest<Genitore> request, Genitore dbEntity) {

        //FIXME: Da sistemare
        Set<Bambino> bambini = request.getEntity().getBambini();

        if(request.isToDelete()) {
            for (Bambino b : bambini) {
                if (b.getGenitori().size() == 1)
                    throw new RuntimeException("Operazione illegale, avrei dei bambini orfani!");
                b.removeGenitore(dbEntity);
                session.update(b);
            }
        }
    }
}
