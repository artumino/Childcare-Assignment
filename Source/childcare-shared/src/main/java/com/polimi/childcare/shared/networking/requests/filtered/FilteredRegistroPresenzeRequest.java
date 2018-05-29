package com.polimi.childcare.shared.networking.requests.filtered;

import com.polimi.childcare.shared.entities.RegistroPresenze;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class FilteredRegistroPresenzeRequest extends FilteredBaseRequest<RegistroPresenze>
{
    public FilteredRegistroPresenzeRequest(int count, int pageNumber, boolean detailed) {
        super(count, pageNumber, detailed);
    }

    public FilteredRegistroPresenzeRequest(int ID, boolean detailed) {
        super(ID, detailed);
    }
}
