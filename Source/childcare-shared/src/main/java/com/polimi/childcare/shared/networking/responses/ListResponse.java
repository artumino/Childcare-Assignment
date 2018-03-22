package com.polimi.childcare.shared.networking.responses;

import java.io.Serializable;
import java.util.ArrayList;

public class ListResponse<T extends Serializable> extends BaseResponse
{
    private ArrayList<T> payload;
    public ListResponse(int code, ArrayList<T> payload) {
        super(code);
        this.payload = payload;
    }

    public ArrayList<T> getPayload() {
        return payload;
    }
}
