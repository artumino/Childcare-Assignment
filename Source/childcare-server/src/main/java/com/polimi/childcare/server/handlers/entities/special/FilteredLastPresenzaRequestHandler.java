package com.polimi.childcare.server.handlers.entities.special;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.database.dao.implementations.RegistroPresenzeDaoImpl;
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

        DatabaseSession.getInstance().execute(session -> {
            CollectionUtils.addAll(list, new RegistroPresenzeDaoImpl(session).getLastPresenze());
            return true;
        });

        //Trasforma i proxy
        DTOUtils.iterableToDTO(list, null);

        return new ListRegistroPresenzeResponse(200, list);
    }
}
