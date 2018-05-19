package com.polimi.childcare.shared.networking.requests.filtered;

import org.jinq.orm.stream.JinqStream;

import java.util.HashMap;
import java.util.List;

public class FilteredContattoRequest extends FilteredBaseRequest
{
    public FilteredContattoRequest(int count, int pageNumber, boolean detailed, List<JinqStream.Where> filters, HashMap<JinqStream.CollectComparable, Boolean> orderBy) {
        super(count, pageNumber, detailed, filters, orderBy);
    }
}
