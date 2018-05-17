package com.polimi.childcare.shared.networking.responses.lists;

import com.polimi.childcare.shared.entities.Fornitore;

import java.util.List;

public class ListFornitoriResponse extends ListResponse<Fornitore>
{
    public ListFornitoriResponse(int code, List<Fornitore> payload) { super(code, payload); }
}
