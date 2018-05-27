package com.polimi.childcare.shared.networking.requests.filtered;

import com.polimi.childcare.shared.entities.Genitore;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class FilteredGenitoriRequest extends FilteredBaseRequest<Genitore>
{
    public FilteredGenitoriRequest(int count, int pageNumber, boolean detailed, List<Predicate<Genitore>> filters, List<Comparator<Genitore>> orderBy) {
        super(count, pageNumber, detailed, filters, orderBy);
    }
}
