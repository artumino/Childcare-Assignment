package com.polimi.childcare.shared.networking.requests.filtered;

import com.polimi.childcare.shared.networking.requests.BaseRequest;

import java.util.HashMap;

public abstract class FilteredBaseRequest extends BaseRequest
{
    private int count;
    private int pageNumber;
    private boolean detailed;
    private HashMap<String,String> filters;
    private HashMap<String,Boolean> orderBy;

    public FilteredBaseRequest(int count, int pageNumber, boolean detailed, HashMap<String, String> filters, HashMap<String, Boolean> orderBy)
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

    public HashMap<String, String> getFilters() {
        return filters;
    }

    public HashMap<String, Boolean> getOrderBy() {
        return orderBy;
    }

    public boolean isDetailed() {
        return detailed;
    }
}
