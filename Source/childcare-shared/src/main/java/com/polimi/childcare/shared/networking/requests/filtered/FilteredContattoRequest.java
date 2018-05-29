package com.polimi.childcare.shared.networking.requests.filtered;

import com.polimi.childcare.shared.entities.Contatto;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class FilteredContattoRequest extends FilteredBaseRequest<Contatto>
{
    public FilteredContattoRequest(int count, int pageNumber, boolean detailed) {
        super(count, pageNumber, detailed);
    }

    public FilteredContattoRequest(int ID, boolean detailed) {
        super(ID, detailed);
    }
}
