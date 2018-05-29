package com.polimi.childcare.shared.networking.requests.filtered;

import com.polimi.childcare.shared.entities.MezzoDiTrasporto;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class FilteredMezzoDiTrasportoRequest extends FilteredBaseRequest<MezzoDiTrasporto>
{
    public FilteredMezzoDiTrasportoRequest(int count, int pageNumber, boolean detailed) {
        super(count, pageNumber, detailed);
    }

    public FilteredMezzoDiTrasportoRequest(int ID, boolean detailed) {
        super(ID, detailed);
    }
}
