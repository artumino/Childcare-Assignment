package com.polimi.childcare.shared.networking.requests.filtered;


import com.polimi.childcare.shared.entities.ReazioneAvversa;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class FilteredReazioneAvversaRequest extends FilteredBaseRequest
{
    public FilteredReazioneAvversaRequest(int count, int pageNumber, boolean detailed, List<Predicate<ReazioneAvversa>> filters, List<Comparator<ReazioneAvversa>> orderBy) {
        super(count, pageNumber, detailed, filters, orderBy);
    }
}
