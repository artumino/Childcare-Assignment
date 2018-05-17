package com.polimi.childcare.shared.networking.responses.lists;

import com.polimi.childcare.shared.entities.Contatto;

import java.util.List;

public class ListContattoResponse extends ListResponse<Contatto>
{
    public ListContattoResponse(int code, List<Contatto> payload) {
        super(code, payload);
    }
}
