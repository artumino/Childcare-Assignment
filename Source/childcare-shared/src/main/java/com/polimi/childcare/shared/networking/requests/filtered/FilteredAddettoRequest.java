package com.polimi.childcare.shared.networking.requests.filtered;


import com.polimi.childcare.shared.entities.Addetto;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

public class FilteredAddettoRequest extends FilteredBaseRequest<Addetto>
{
    public FilteredAddettoRequest(int count, int pageNumber, boolean detailed) {
        super(count, pageNumber, detailed);
    }

    public FilteredAddettoRequest(int ID, boolean detailed) {
        super(ID, detailed);
    }
}
