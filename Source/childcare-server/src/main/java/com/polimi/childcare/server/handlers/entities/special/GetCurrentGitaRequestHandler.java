package com.polimi.childcare.server.handlers.entities.special;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.Gita;
import com.polimi.childcare.shared.networking.requests.special.GetCurrentGitaRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListGitaResponse;
import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;

public class GetCurrentGitaRequestHandler implements IRequestHandler<GetCurrentGitaRequest>
{
    @Override
    public BaseResponse processRequest(GetCurrentGitaRequest request)
    {
        if(request.getUtcInstantEpochSeconds()  < 0)
            return new BadRequestResponse();

        ArrayList<Gita> gita = new ArrayList<>();

        DatabaseSession.getInstance().execute(session ->{

            CriteriaBuilder criteriaBuilder = session.getSession().getCriteriaBuilder();
            CriteriaQuery<Gita> query = criteriaBuilder.createQuery(Gita.class);
            Root<Gita> root = query.from(Gita.class);
            query.where(criteriaBuilder.greaterThan(root.get("DataFine"),
                    request.getUtcInstantEpochSeconds()), criteriaBuilder.lessThan(root.get("DataInizio"), request.getUtcInstantEpochSeconds()));
            CollectionUtils.addAll(gita, session.getSession().createQuery(query).iterate());

            return true;
        });

        return new ListGitaResponse(200, gita);
    }
}
