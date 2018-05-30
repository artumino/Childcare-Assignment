package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.shared.entities.Gita;
import com.polimi.childcare.shared.entities.Gruppo;
import com.polimi.childcare.shared.entities.MezzoDiTrasporto;
import com.polimi.childcare.shared.entities.PianoViaggi;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;

public class PianoViaggiRequestHandlerSet extends GenericSetEntityRequestHandler<SetEntityRequest<PianoViaggi>, PianoViaggi>
{
    @Override
    protected Class<PianoViaggi> getQueryClass() {
        return PianoViaggi.class;
    }

    @Override
    protected void doPreSetChecks(DatabaseSession.DatabaseSessionInstance session, SetEntityRequest<PianoViaggi> request, PianoViaggi dbEntity)
    {
        if (dbEntity != null && request.getOldHashCode() == dbEntity.consistecyHashCode())
        {
            if (!request.isToDelete())
            {
                if(request.getEntity().getGruppo() == null || request.getEntity().getGita() == null)
                    throw new RuntimeException("Gita e/o Gruppo nulli!");

                DBHelper.updateManyToOne(request.getEntity().asPianiViaggioGitaRelation(), Gita.class, session);
                DBHelper.updateManyToOne(request.getEntity().asPianiViaggioGruppoRelation(), Gruppo.class, session);
                DBHelper.updateManyToOne(request.getEntity().asPianiViaggioMezzoDiTrasportoRelation(), MezzoDiTrasporto.class, session);
            }
        }
    }
}