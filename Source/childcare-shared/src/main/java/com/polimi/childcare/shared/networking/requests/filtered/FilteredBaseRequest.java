package com.polimi.childcare.shared.networking.requests.filtered;

import com.polimi.childcare.shared.networking.requests.BaseRequest;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public abstract class FilteredBaseRequest<T> extends BaseRequest
{
    private int count;
    private int pageNumber;
    private boolean detailed;
    private List<Predicate<T>> filters;
    private List<Comparator<T>> orderBy;

    public FilteredBaseRequest(int count, int pageNumber, boolean detailed, List<Predicate<T>> filters, List<Comparator<T>> orderBy)
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

    public List<Predicate<T>> getFilters() {
        return filters;
    }

    public List<Comparator<T>> getOrderBy() {
        return orderBy;
    }

    public boolean isDetailed() {
        return detailed;
    }
}
