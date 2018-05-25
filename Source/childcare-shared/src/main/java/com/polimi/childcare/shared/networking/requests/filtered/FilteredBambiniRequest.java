package com.polimi.childcare.shared.networking.requests.filtered;

import com.polimi.childcare.shared.entities.Bambino;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class FilteredBambiniRequest extends FilteredBaseRequest
{
    public FilteredBambiniRequest(int count, int pageNumber, boolean detailed, List<Predicate<Bambino>> filters, List<Comparator<Bambino>> orderBy) {
        super(count, pageNumber, detailed, filters, orderBy);
    }
}
