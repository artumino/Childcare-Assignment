package com.polimi.childcare.shared.networking.requests.filtered;

import com.polimi.childcare.shared.entities.Pediatra;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class FilteredPediatraRequest extends FilteredBaseRequest
{
    public FilteredPediatraRequest(int count, int pageNumber, boolean detailed, List<Predicate<Pediatra>> filters, List<Comparator<Pediatra>> orderBy) {
        super(count, pageNumber, detailed, filters, orderBy);
    }
}
