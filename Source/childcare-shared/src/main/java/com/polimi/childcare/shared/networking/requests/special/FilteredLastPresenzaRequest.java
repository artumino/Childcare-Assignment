package com.polimi.childcare.shared.networking.requests.special;

import com.polimi.childcare.shared.entities.RegistroPresenze;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredBaseRequest;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class FilteredLastPresenzaRequest extends FilteredBaseRequest<RegistroPresenze>
{
    public FilteredLastPresenzaRequest(int count, int pageNumber, boolean detailed) {
        super(count, pageNumber, detailed);
    }

    public FilteredLastPresenzaRequest(int ID, boolean detailed) {
        super(ID, detailed);
    }
}
