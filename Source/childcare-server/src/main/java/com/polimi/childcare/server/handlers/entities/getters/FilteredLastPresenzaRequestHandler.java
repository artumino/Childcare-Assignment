package com.polimi.childcare.server.handlers.entities.getters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.dto.DTOUtils;
import com.polimi.childcare.shared.entities.RegistroPresenze;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredLastPresenzaRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListRegistroPresenzeResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.mapping.Collection;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class FilteredLastPresenzaRequestHandler implements IRequestHandler<FilteredLastPresenzaRequest>
{
    @Override
    @SuppressWarnings("Unchecked")
    public BaseResponse processRequest(FilteredLastPresenzaRequest request)
    {
        if(request.getCount() < 0 || request.getPageNumber() < 0)
            return new BadRequestResponse();

        List<RegistroPresenze> list = new ArrayList<>();

        DatabaseSession.getInstance().execute(session -> {
            CriteriaBuilder queryBuilder = session.getSession().getCriteriaBuilder();
            CriteriaQuery<Object[]> criteriaQuery = queryBuilder.createQuery(Object[].class);
            Root<RegistroPresenze> registroPresenzeRoot = criteriaQuery.from(RegistroPresenze.class);
            criteriaQuery.multiselect(
                    registroPresenzeRoot.get("ID"),
                    queryBuilder.max(registroPresenzeRoot.get("TimeStamp"))
            );
            criteriaQuery.groupBy(registroPresenzeRoot.get("bambino"));
            Query query = session.getSession().createQuery(criteriaQuery);
            List<Object[]> resultQuery = query.getResultList();

            if(resultQuery != null && resultQuery.size() > 0)
            {
                List<Integer> lastPresenzeIDs = new ArrayList<>(resultQuery.size());
                for(Object[] row : resultQuery)
                    lastPresenzeIDs.add((Integer)row[0]);

                CriteriaQuery<RegistroPresenze> criteriaSelectIn = queryBuilder.createQuery(RegistroPresenze.class);
                Root<RegistroPresenze> criteriaSelectFrom = criteriaSelectIn.from(RegistroPresenze.class);
                Path<Integer> ID = criteriaSelectFrom.get("ID");
                criteriaSelectIn.select(criteriaSelectFrom);
                criteriaSelectIn.where(ID.in(lastPresenzeIDs));
                CollectionUtils.addAll(list, session.getSession().createQuery(criteriaSelectIn).getResultStream().iterator());
            }
            return true;
        });

        //Trasforma i proxy
        DTOUtils.iterableToDTO(list, null);

        return new ListRegistroPresenzeResponse(200, list);
    }
}
