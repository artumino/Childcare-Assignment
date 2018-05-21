package com.polimi.childcare.shared.networking.responses.lists;

import com.polimi.childcare.shared.entities.Menu;

import java.util.List;

public class ListMenuResponse extends ListResponse<Menu>
{
    public ListMenuResponse(int code, List<Menu> payload) {
        super(code, payload);
    }
}
