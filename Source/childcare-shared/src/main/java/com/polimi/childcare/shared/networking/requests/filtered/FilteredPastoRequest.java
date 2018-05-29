package com.polimi.childcare.shared.networking.requests.filtered;

import com.polimi.childcare.shared.entities.Pasto;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class FilteredPastoRequest extends FilteredBaseRequest<Pasto>
{
    public FilteredPastoRequest(int count, int pageNumber, boolean detailed) {
        super(count, pageNumber, detailed);
    }

    public FilteredPastoRequest(int ID, boolean detailed) {
        super(ID, detailed);
    }
}
