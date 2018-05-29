package com.polimi.childcare.shared.networking.requests.filtered;

import com.polimi.childcare.shared.entities.QuantitaPasto;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class FilteredQuantitaPastoRequest extends FilteredBaseRequest<QuantitaPasto>
{
    public FilteredQuantitaPastoRequest(int count, int pageNumber, boolean detailed) {
        super(count, pageNumber, detailed);
    }

    public FilteredQuantitaPastoRequest(int ID, boolean detailed) {
        super(ID, detailed);
    }
}
