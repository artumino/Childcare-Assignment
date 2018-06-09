package com.polimi.childcare.server.handlers.entities.special;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.dto.DTOUtils;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.networking.requests.special.GetBambiniSenzaGruppoRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListBambiniResponse;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GetBambiniSenzaGruppoRequestHandler implements IRequestHandler<GetBambiniSenzaGruppoRequest>
{
    @Override
    public BaseResponse processRequest(GetBambiniSenzaGruppoRequest request)
    {
        List<Bambino> bambiniOrfani = new ArrayList<>();
        DatabaseSession.getInstance().execute(session -> {
            CollectionUtils.addAll(bambiniOrfani, session.stream(Bambino.class).filter(p -> p.getGruppo() == null).iterator());
            return true;
        });

        DTOUtils.iterableToDTO(bambiniOrfani, new ArrayList<>());

        return new ListBambiniResponse(200, bambiniOrfani);
    }
}
