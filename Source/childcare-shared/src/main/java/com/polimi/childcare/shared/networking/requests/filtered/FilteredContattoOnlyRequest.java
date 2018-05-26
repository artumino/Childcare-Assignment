package com.polimi.childcare.shared.networking.requests.filtered;

import com.polimi.childcare.shared.entities.Contatto;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class FilteredContattoOnlyRequest extends FilteredBaseRequest
{
    public FilteredContattoOnlyRequest(int count, int pageNumber, boolean detailed, List<Predicate<Contatto>> filters, List<Comparator<Contatto>> orderBy)
    {
        super(count, pageNumber, detailed, filters, orderBy);
    }
}
