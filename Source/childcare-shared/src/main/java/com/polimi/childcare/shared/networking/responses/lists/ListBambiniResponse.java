package com.polimi.childcare.shared.networking.responses.lists;

import com.polimi.childcare.shared.entities.Bambino;

import java.util.List;

public class ListBambiniResponse extends ListResponse<Bambino>
{
    public ListBambiniResponse(int code, List<Bambino> payload) {
        super(code, payload);
    }
}
