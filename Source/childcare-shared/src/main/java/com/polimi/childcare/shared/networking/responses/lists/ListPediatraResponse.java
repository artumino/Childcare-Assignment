package com.polimi.childcare.shared.networking.responses.lists;

import com.polimi.childcare.shared.entities.Pediatra;

import java.util.List;

public class ListPediatraResponse extends ListResponse<Pediatra>
{
    public ListPediatraResponse(int code, List<Pediatra> payload) {
        super(code, payload);
    }
}
