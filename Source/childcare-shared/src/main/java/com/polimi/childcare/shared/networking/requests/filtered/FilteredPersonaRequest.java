package com.polimi.childcare.shared.networking.requests.filtered;

import com.polimi.childcare.shared.entities.Persona;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class FilteredPersonaRequest extends FilteredBaseRequest<Persona>
{
    public FilteredPersonaRequest(int count, int pageNumber, boolean detailed) {
        super(count, pageNumber, detailed);
    }

    public FilteredPersonaRequest(int ID, boolean detailed) {
        super(ID, detailed);
    }
}
