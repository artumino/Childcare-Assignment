package com.polimi.childcare.shared.networking.requests.filtered;


import com.polimi.childcare.shared.entities.ReazioneAvversa;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class FilteredReazioneAvversaRequest extends FilteredBaseRequest<ReazioneAvversa>
{
    public FilteredReazioneAvversaRequest(int count, int pageNumber, boolean detailed) {
        super(count, pageNumber, detailed);
    }

    public FilteredReazioneAvversaRequest(int ID, boolean detailed) {
        super(ID, detailed);
    }
}
