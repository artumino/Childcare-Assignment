package com.polimi.childcare.shared.networking.responses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListResponse<T extends Serializable> extends BaseResponse
{
    private List<T> payload;
    public ListResponse(int code, List<T> payload) {
        super(code);
        this.payload = payload;
    }

    public List<T> getPayload() {
        return payload;
    }
}
