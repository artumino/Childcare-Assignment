package com.polimi.childcare.shared.networking.requests.filtered;

import com.polimi.childcare.shared.networking.requests.BaseRequest;
import org.jinq.orm.stream.JinqStream.CollectComparable;
import org.jinq.orm.stream.JinqStream.Where;

import java.util.HashMap;
import java.util.List;

public abstract class FilteredBaseRequest extends BaseRequest
{
    private int count;
    private int pageNumber;
    private boolean detailed;
    private List<Where> filters;
    private HashMap<CollectComparable,Boolean> orderBy;

    public FilteredBaseRequest(int count, int pageNumber, boolean detailed, List<Where> filters, HashMap<CollectComparable,Boolean> orderBy)
    {
        this.count = count;
        this.pageNumber = pageNumber;
        this.filters = filters;
        this.orderBy = orderBy;
        this.detailed = detailed;
    }

    public int getCount() {
        return count;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public List<Where> getFilters() {
        return filters;
    }

    public HashMap<CollectComparable, Boolean> getOrderBy() {
        return orderBy;
    }

    public boolean isDetailed() {
        return detailed;
    }
}
