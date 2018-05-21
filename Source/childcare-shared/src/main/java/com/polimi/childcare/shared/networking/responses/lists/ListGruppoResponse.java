package com.polimi.childcare.shared.networking.responses.lists;

import com.polimi.childcare.shared.entities.Gruppo;

import java.util.List;

public class ListGruppoResponse extends ListResponse<Gruppo>
{
    public ListGruppoResponse(int code, List<Gruppo> payload) {
        super(code, payload);
    }
}
