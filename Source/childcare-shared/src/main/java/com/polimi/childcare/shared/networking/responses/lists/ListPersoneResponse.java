package com.polimi.childcare.shared.networking.responses.lists;

import com.polimi.childcare.shared.entities.Persona;
import java.util.List;

public class ListPersoneResponse extends ListResponse<Persona>
{
    public ListPersoneResponse(int code, List<Persona> payload) {
        super(code, payload);
    }
}
