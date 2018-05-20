package com.polimi.childcare.shared.networking.responses.lists;

import com.polimi.childcare.shared.entities.Genitore;

import java.util.List;

public class ListGenitoriResponse extends ListResponse<Genitore>
{
    public ListGenitoriResponse(int code, List<Genitore> payload) { super(code, payload); }
}