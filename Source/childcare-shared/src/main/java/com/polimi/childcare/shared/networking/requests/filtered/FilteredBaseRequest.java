package com.polimi.childcare.shared.networking.requests.filtered;

import com.polimi.childcare.shared.networking.requests.BaseRequest;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

public abstract class FilteredBaseRequest<T> extends BaseRequest
{
    private int ID;
    private int count;
    private int pageNumber;
    private boolean detailed;
    private boolean group;

    public FilteredBaseRequest(int count, int pageNumber, boolean detailed)
    {
        this.count = count;
        this.pageNumber = pageNumber;
        this.detailed = detailed;
        this.group = true;
    }

    public FilteredBaseRequest(int ID, boolean detailed)
    {
        this.ID = ID;
        this.count = 1;
        this.pageNumber = 0;
        this.detailed = detailed;
        this.group = false;
    }

    public int getID() {
        return ID;
    }

    public int getCount() {
        return count;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public boolean isDetailed() {
        return detailed;
    }

    public boolean isGroup() {
        return group;
    }
}
