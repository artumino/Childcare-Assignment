package com.polimi.childcare.shared.networking.requests.filtered;

import org.jinq.orm.stream.JinqStream;

import java.util.HashMap;
import java.util.List;

public class FilteredReazioneAvversaRequest extends FilteredBaseRequest
{
    public FilteredReazioneAvversaRequest(int count, int pageNumber, boolean detailed, List<JinqStream.Where> filters, HashMap<JinqStream.CollectComparable, Boolean> orderBy) {
        super(count, pageNumber, detailed, filters, orderBy);
    }
}
