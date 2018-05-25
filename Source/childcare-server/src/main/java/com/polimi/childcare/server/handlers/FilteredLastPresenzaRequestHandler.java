package com.polimi.childcare.server.handlers;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.dto.DTOUtils;
import com.polimi.childcare.shared.entities.RegistroPresenze;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredLastPresenzaRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListRegistroPresenzeResponse;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

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
            CriteriaQuery criteriaQuery = queryBuilder.createQuery();
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

                list.addAll(session.query(RegistroPresenze.class).where(rp -> lastPresenzeIDs.contains(rp.getID())).toList());
            }
            return true;
        });

        //Trasforma i proxy
        DTOUtils.iterableToDTO(list);

        return new ListRegistroPresenzeResponse(200, list);
    }
}
