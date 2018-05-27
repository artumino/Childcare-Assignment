package com.polimi.childcare.shared.networking.requests.filtered;

import com.polimi.childcare.shared.entities.Gita;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class FilteredGitaRequest extends FilteredBaseRequest<Gita>
{
    public FilteredGitaRequest(int count, int pageNumber, boolean detailed, List<Predicate<Gita>> filters, List<Comparator<Gita>> orderBy) {
        super(count, pageNumber, detailed, filters, orderBy);
    }
}
