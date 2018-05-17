package com.polimi.childcare.shared.networking.requests;

import java.util.HashMap;

public class PersonaRequest extends BaseRequest
{
    private int count;
    private int pageNumber;
    private HashMap<String,Boolean> orderBy;

    public PersonaRequest(int count, int pageNumber, HashMap<String, Boolean> orderBy) {
        this.count = count;
        this.pageNumber = pageNumber;
        this.orderBy = orderBy;
    }

    public int getCount() { return count; }

    public int getPageNumber() { return pageNumber; }

    public HashMap<String, Boolean> getOrderBy() { return orderBy; }
}
