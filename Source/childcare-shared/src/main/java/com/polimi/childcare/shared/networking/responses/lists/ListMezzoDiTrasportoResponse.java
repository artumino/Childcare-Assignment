package com.polimi.childcare.shared.networking.responses.lists;

import com.polimi.childcare.shared.entities.MezzoDiTrasporto;

import java.util.List;

public class ListMezzoDiTrasportoResponse extends ListResponse<MezzoDiTrasporto>
{
    public ListMezzoDiTrasportoResponse(int code, List<MezzoDiTrasporto> payload) {
        super(code, payload);
    }
}
