package com.polimi.childcare.shared.networking.requests.filtered;

import com.polimi.childcare.shared.entities.QuantitaPasto;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class FilteredQuantitaPastoRequest extends FilteredBaseRequest
{
    public FilteredQuantitaPastoRequest(int count, int pageNumber, boolean detailed, List<Predicate<QuantitaPasto>> filters, List<Comparator<QuantitaPasto>> orderBy) {
        super(count, pageNumber, detailed, filters, orderBy);
    }
}
