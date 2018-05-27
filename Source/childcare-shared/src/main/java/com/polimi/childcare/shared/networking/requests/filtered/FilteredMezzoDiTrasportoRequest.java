package com.polimi.childcare.shared.networking.requests.filtered;

import com.polimi.childcare.shared.entities.MezzoDiTrasporto;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class FilteredMezzoDiTrasportoRequest extends FilteredBaseRequest<MezzoDiTrasporto>
{
    public FilteredMezzoDiTrasportoRequest(int count, int pageNumber, boolean detailed, List<Predicate<MezzoDiTrasporto>> filters, List<Comparator<MezzoDiTrasporto>> orderBy) {
        super(count, pageNumber, detailed, filters, orderBy);
    }
}
