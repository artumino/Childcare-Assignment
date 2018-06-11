package com.polimi.childcare.shared.networking.requests.special;

import com.polimi.childcare.shared.entities.Gita;
import com.polimi.childcare.shared.networking.requests.BaseRequest;

public class StartPresenzaCheckRequest extends BaseRequest
{
    private Gita gita;

    public StartPresenzaCheckRequest(Gita gita) {
        this.gita = gita;
    }

    public Gita getGita() {
        return gita;
    }
}
