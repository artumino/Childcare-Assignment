package com.polimi.childcare.shared.networking.requests.filtered;

import com.polimi.childcare.shared.entities.PianoViaggi;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class FilteredPianoViaggiRequest extends FilteredBaseRequest<PianoViaggi>
{
    public FilteredPianoViaggiRequest(int count, int pageNumber, boolean detailed) {
        super(count, pageNumber, detailed);
    }

    public FilteredPianoViaggiRequest(int ID, boolean detailed) {
        super(ID, detailed);
    }
}
