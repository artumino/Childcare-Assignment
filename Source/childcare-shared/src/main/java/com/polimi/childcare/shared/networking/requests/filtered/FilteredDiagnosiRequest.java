package com.polimi.childcare.shared.networking.requests.filtered;

import com.polimi.childcare.shared.entities.Diagnosi;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class FilteredDiagnosiRequest extends FilteredBaseRequest<Diagnosi>
{
    public FilteredDiagnosiRequest(int count, int pageNumber, boolean detailed, List<Predicate<Diagnosi>> filters, List<Comparator<Diagnosi>> orderBy) {
        super(count, pageNumber, detailed, filters, orderBy);
    }
}
