package com.polimi.childcare.shared.networking.responses;

import java.util.ArrayList;

public class ArrayListResponse extends BaseResponse
{
    private ArrayList<Integer> payload;
    public ArrayListResponse(int code, ArrayList<Integer> payload) {
        super(code);
        this.payload = payload;
    }

    public ArrayList<Integer> getPayload() {
        return payload;
    }
}
