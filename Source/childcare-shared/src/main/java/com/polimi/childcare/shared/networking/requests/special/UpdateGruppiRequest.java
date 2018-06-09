package com.polimi.childcare.shared.networking.requests.special;

import com.polimi.childcare.shared.entities.Gruppo;
import com.polimi.childcare.shared.networking.requests.BaseRequest;

import java.util.List;

public class UpdateGruppiRequest extends BaseRequest
{
    private List<Gruppo> newGruppi;

    public UpdateGruppiRequest(List<Gruppo> newGruppi) {
        this.newGruppi = newGruppi;
    }

    public List<Gruppo> getNewGruppi() {
        return newGruppi;
    }
}
