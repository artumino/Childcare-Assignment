package com.polimi.childcare.shared.networking.requests.filtered;

import com.polimi.childcare.shared.entities.Fornitore;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class FilteredFornitoriRequest extends FilteredBaseRequest
{
    public FilteredFornitoriRequest(int count, int pageNumber, boolean detailed, List<Predicate<Fornitore>> filters, List<Comparator<Fornitore>> orderBy) {
        super(count, pageNumber, detailed, filters, orderBy);
    }
}
