package com.polimi.childcare.shared.networking.requests.filtered;

import com.polimi.childcare.shared.entities.Gita;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class FilteredGitaRequest extends FilteredBaseRequest<Gita>
{
    public FilteredGitaRequest(int count, int pageNumber, boolean detailed) {
        super(count, pageNumber, detailed);
    }

    public FilteredGitaRequest(int ID, boolean detailed) {
        super(ID, detailed);
    }
}
