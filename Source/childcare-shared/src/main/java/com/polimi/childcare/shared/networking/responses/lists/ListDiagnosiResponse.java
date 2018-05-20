package com.polimi.childcare.shared.networking.responses.lists;

import com.polimi.childcare.shared.entities.Diagnosi;

import java.util.List;

public class ListDiagnosiResponse extends ListResponse<Diagnosi>
{
    public ListDiagnosiResponse(int code, List<Diagnosi> payload) { super(code, payload); }
}
