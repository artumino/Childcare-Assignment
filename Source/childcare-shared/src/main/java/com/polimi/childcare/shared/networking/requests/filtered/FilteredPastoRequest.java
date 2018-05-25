package com.polimi.childcare.shared.networking.requests.filtered;

import com.polimi.childcare.shared.entities.Pasto;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class FilteredPastoRequest extends FilteredBaseRequest
{
    public FilteredPastoRequest(int count, int pageNumber, boolean detailed, List<Predicate<Pasto>> filters, List<Comparator<Pasto>> orderBy) {
        super(count, pageNumber, detailed, filters, orderBy);
    }
}
