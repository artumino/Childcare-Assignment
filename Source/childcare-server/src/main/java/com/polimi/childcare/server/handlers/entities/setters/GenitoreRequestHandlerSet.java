package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.entities.Genitore;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;

import java.util.Set;

public class GenitoreRequestHandlerSet extends GenericSetEntityRequestHandler<SetEntityRequest<Genitore>, Genitore>
{
    @Override
    protected Class<Genitore> getQueryClass() {
        return Genitore.class;
    }

    @Override
    protected void doPreSetChecks(DatabaseSession.DatabaseSessionInstance session, SetEntityRequest<Genitore> request, Genitore dbEntity)
    {

        if (dbEntity != null && request.getOldHashCode() == dbEntity.consistecyHashCode())
        {
            if (!request.isToDelete())
            {
                if(request.getEntity().getNome() == null ||
                        request.getEntity().getCognome() == null ||
                        request.getEntity().getCodiceFiscale() == null ||
                        request.getEntity().getDataNascita() == null ||
                        request.getEntity().getStato() == null ||
                        request.getEntity().getComune() == null ||
                        request.getEntity().getProvincia() == null ||
                        request.getEntity().getCittadinanza() == null ||
                        request.getEntity().getResidenza() == null ||
                        request.getEntity().getSesso() == null)
                    throw new RuntimeException("Un campo obbligatorio è null!");

                DBHelper.updateManyToManyOwned(request.getEntity().asGenitoriBambiniRelation(), dbEntity.asGenitoriBambiniRelation(), Bambino.class, session);
            }

            else
            {
                Set<Bambino> bambini = request.getEntity().getBambini();

                for (Bambino b : bambini) {
                    if (b.getGenitori().size() == 1)
                        throw new RuntimeException("Operazione illegale, avrei dei bambini orfani!");
                    b.removeGenitore(dbEntity);
                    session.update(b);
                }
            }
        }
    }
}
