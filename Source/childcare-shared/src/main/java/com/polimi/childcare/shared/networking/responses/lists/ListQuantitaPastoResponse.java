package com.polimi.childcare.shared.networking.responses.lists;

import com.polimi.childcare.shared.entities.QuantitaPasto;

import java.util.List;

public class ListQuantitaPastoResponse extends ListResponse<QuantitaPasto>
{
    public ListQuantitaPastoResponse(int code, List<QuantitaPasto> payload) {
        super(code, payload);
    }
}
