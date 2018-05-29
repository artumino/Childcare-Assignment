package com.polimi.childcare.shared.networking.requests.filtered;

import com.polimi.childcare.shared.entities.Gruppo;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class FilteredGruppoRequest extends FilteredBaseRequest<Gruppo>
{
    public FilteredGruppoRequest(int count, int pageNumber, boolean detailed) {
        super(count, pageNumber, detailed);
    }

    public FilteredGruppoRequest(int ID, boolean detailed) {
        super(ID, detailed);
    }
}
