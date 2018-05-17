package com.polimi.childcare.shared.networking.responses.lists;

import com.polimi.childcare.shared.entities.Pasto;
import java.util.List;

public class ListPastiResponse extends ListResponse<Pasto>
{
    public ListPastiResponse (int code, List<Pasto> payload) {
        super(code, payload);
    }
}
