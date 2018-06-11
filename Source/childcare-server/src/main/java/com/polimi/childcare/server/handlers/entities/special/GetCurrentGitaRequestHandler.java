package com.polimi.childcare.server.handlers.entities.special;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.dto.DTOUtils;
import com.polimi.childcare.shared.entities.Gita;
import com.polimi.childcare.shared.networking.requests.special.GetCurrentGitaRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListGitaResponse;
import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
            LocalDate currentMoment = LocalDateTime.ofEpochSecond(request.getUtcInstantEpochSeconds(), 0, ZoneOffset.systemDefault().getRules().getOffset(Instant.now())).toLocalDate();
            query.where(criteriaBuilder.greaterThanOrEqualTo(root.get("DataFine"), currentMoment),
                        criteriaBuilder.lessThanOrEqualTo(root.get("DataInizio"), currentMoment));
            CollectionUtils.addAll(gita, session.getSession().createQuery(query).iterate());

            DBHelper.recursiveIterableIntitialize(gita);
            return true;
        });

        DTOUtils.iterableToDTO(gita, new ArrayList<>());
        return new ListGitaResponse(200, gita);
    }
}
