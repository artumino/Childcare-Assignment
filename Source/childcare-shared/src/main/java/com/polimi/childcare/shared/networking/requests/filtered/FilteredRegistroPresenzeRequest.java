package com.polimi.childcare.shared.networking.requests.filtered;

import com.polimi.childcare.shared.entities.RegistroPresenze;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class FilteredRegistroPresenzeRequest extends FilteredBaseRequest<RegistroPresenze>
{
    public FilteredRegistroPresenzeRequest(int count, int pageNumber, boolean detailed, List<Predicate<RegistroPresenze>> filters, List<Comparator<RegistroPresenze>> orderBy) {
        super(count, pageNumber, detailed, filters, orderBy);
    }
}
