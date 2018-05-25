package com.polimi.childcare.shared.networking.requests.filtered;

import com.polimi.childcare.shared.entities.Gruppo;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class FilteredGruppoRequest extends FilteredBaseRequest
{
    public FilteredGruppoRequest(int count, int pageNumber, boolean detailed, List<Predicate<Gruppo>> filters, List<Comparator<Gruppo>> orderBy) {
        super(count, pageNumber, detailed, filters, orderBy);
    }
}
