package com.polimi.childcare.shared.networking.responses.lists;

import com.polimi.childcare.shared.entities.RegistroPresenze;

import java.util.List;

public class ListRegistroPresenzeResponse extends ListResponse<RegistroPresenze>
{
    public ListRegistroPresenzeResponse(int code, List<RegistroPresenze> payload) {
        super(code, payload);
    }
}
