package com.polimi.childcare.shared.networking.requests.special;

import com.polimi.childcare.shared.entities.Persona;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredBaseRequest;

public class GetPersoneWithDisagnosiRequest extends FilteredBaseRequest<Persona>
{

    public GetPersoneWithDisagnosiRequest(int count, int pageNumber, boolean detailed) {
        super(count, pageNumber, detailed);
    }

    public GetPersoneWithDisagnosiRequest(int ID, boolean detailed) {
        super(ID, detailed);
    }
}
