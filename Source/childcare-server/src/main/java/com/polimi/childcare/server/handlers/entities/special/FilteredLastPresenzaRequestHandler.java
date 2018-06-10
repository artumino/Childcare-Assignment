package com.polimi.childcare.server.handlers.entities.special;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.handlers.entities.getters.FilteredRequestHandler;
import com.polimi.childcare.shared.dto.DTOUtils;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.entities.RegistroPresenze;
import com.polimi.childcare.shared.networking.requests.special.FilteredLastPresenzaRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListRegistroPresenzeResponse;
import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FilteredLastPresenzaRequestHandler extends FilteredRequestHandler<FilteredLastPresenzaRequest, RegistroPresenze>
{
    @Override
    @SuppressWarnings("Unchecked")
    public BaseResponse processRequest(FilteredLastPresenzaRequest request)
    {
        if(request.getCount() < 0 || request.getPageNumber() < 0)
            return new BadRequestResponse();

        List<RegistroPresenze> list = new ArrayList<>();

        /*
        Query eseguita:

        SELECT *
        FROM RegistroPresenze
        WHERE (Bambino_FK, TimeStamp) IN (
          SELECT Bambino_FK, MAX(TimeStamp) as `TimeStamp`
          FROM RegistroPresenze
          WHERE TimeStamp > DateTime.Date.Now
          GROUP BY Bambino_FK
        )
         */
        DatabaseSession.getInstance().execute(session -> {
            CriteriaBuilder queryBuilder = session.getSession().getCriteriaBuilder();

            CriteriaQuery<Object[]> subQuery = queryBuilder.createQuery(Object[].class);
            Root<RegistroPresenze> registroPresenzeRoot = subQuery.from(RegistroPresenze.class);
            subQuery.multiselect(
                    registroPresenzeRoot.get("bambino"),
                    queryBuilder.max(registroPresenzeRoot.get("TimeStamp"))
            );
            subQuery.where(queryBuilder.greaterThanOrEqualTo(registroPresenzeRoot.get("TimeStamp"), LocalDateTime.now().toLocalDate().atStartOfDay()));
            subQuery.groupBy(registroPresenzeRoot.get("bambino"));
            Iterator<Object[]> objects = session.getSession().createQuery(subQuery).getResultStream().iterator();

            while(objects.hasNext())
            {
                Object[] tuple = objects.next();

                CriteriaQuery<RegistroPresenze> criteriaSelectIn = queryBuilder.createQuery(RegistroPresenze.class);
                Root<RegistroPresenze> criteriaSelectFrom = criteriaSelectIn.from(RegistroPresenze.class);
                Path<Bambino> bambino = criteriaSelectFrom.get("bambino");
                Path<LocalDateTime> timestamp = criteriaSelectFrom.get("TimeStamp");
                criteriaSelectIn.where(queryBuilder.equal(bambino, tuple[0]), queryBuilder.equal(timestamp, tuple[1]));
                CollectionUtils.addAll(list, session.getSession().createQuery(criteriaSelectIn).getResultStream().iterator());
            }


            return true;
        });

        //Trasforma i proxy
        DTOUtils.iterableToDTO(list, null);

        return new ListRegistroPresenzeResponse(200, list);
    }
}
