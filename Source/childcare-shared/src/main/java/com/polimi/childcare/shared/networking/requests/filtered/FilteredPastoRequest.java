package com.polimi.childcare.shared.networking.requests.filtered;

import com.polimi.childcare.shared.entities.Pasto;
import com.polimi.childcare.shared.networking.responses.lists.ListResponse;

import java.util.List;

public class FilteredPastoRequest extends ListResponse<Pasto>
{
    public FilteredPastoRequest(int code, List<Pasto> payload) {
        super(code, payload);
    }
}
