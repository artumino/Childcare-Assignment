package com.polimi.childcare.shared.networking.requests.filtered;

import java.util.HashMap;

public class FilteredPastoRequest extends FilteredBaseRequest
{
    public FilteredPastoRequest(int count, int pageNumber, boolean detailed, HashMap<String, String> filters, HashMap<String, Boolean> orderBy) {
        super(count, pageNumber, detailed, filters, orderBy);
    }
}
