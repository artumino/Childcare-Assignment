package com.polimi.childcare.shared.networking.responses;

import java.io.Serializable;

public class BaseResponse implements Serializable
{
    private int code; //Codici errore (stessi di HTTP)

    public BaseResponse(int code)
    {
        this.code = code;
    }

    public int getCode() { return code; }
}
