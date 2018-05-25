package com.polimi.childcare.shared.networking.requests.filtered;

import com.polimi.childcare.shared.entities.Persona;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class FilteredPersonaRequest extends FilteredBaseRequest
{
    public FilteredPersonaRequest(int count, int pageNumber, boolean detailed, List<Predicate<Persona>> filters, List<Comparator<Persona>> orderBy) {
        super(count, pageNumber, detailed, filters, orderBy);
    }
}
