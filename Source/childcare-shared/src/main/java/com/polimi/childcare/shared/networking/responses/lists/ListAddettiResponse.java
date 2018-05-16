package com.polimi.childcare.shared.networking.responses.lists;

import com.polimi.childcare.shared.entities.Addetto;

import java.util.List;

public class ListAddettiResponse extends ListResponse<Addetto>
{
    public ListAddettiResponse(int code, List<Addetto> payload) {
        super(code, payload);
    }
}
