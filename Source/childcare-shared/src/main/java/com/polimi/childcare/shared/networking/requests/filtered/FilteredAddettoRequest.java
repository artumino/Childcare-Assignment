package com.polimi.childcare.shared.networking.requests.filtered;

import com.polimi.childcare.shared.entities.Addetto;
import com.polimi.childcare.shared.networking.responses.lists.ListResponse;

import java.util.List;

public class FilteredAddettoRequest extends ListResponse<Addetto>
{
    public FilteredAddettoRequest(int code, List<Addetto> payload) {
        super(code, payload);
    }
}
