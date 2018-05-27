package com.polimi.childcare.shared.networking.requests.filtered;

import com.polimi.childcare.shared.entities.Menu;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class FilteredMenuRequest extends FilteredBaseRequest<Menu>
{
    public FilteredMenuRequest(int count, int pageNumber, boolean detailed, List<Predicate<Menu>> filters, List<Comparator<Menu>> orderBy) {
        super(count, pageNumber, detailed, filters, orderBy);
    }
}
