package com.polimi.childcare.shared.networking.requests.filtered;

import com.polimi.childcare.shared.entities.Pediatra;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class FilteredPediatraRequest extends FilteredBaseRequest<Pediatra>
{
    public FilteredPediatraRequest(int count, int pageNumber, boolean detailed) {
        super(count, pageNumber, detailed);
    }

    public FilteredPediatraRequest(int ID, boolean detailed) {
        super(ID, detailed);
    }
}
