package com.polimi.childcare.shared.networking.responses.lists;

import java.io.Serializable;
import java.util.ArrayList;

public class SerializableListResponse extends ListResponse<Serializable>
{
    public SerializableListResponse(int code, ArrayList<Serializable> payload) {
        super(code, payload);
    }
}
