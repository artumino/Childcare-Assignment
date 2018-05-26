package com.polimi.childcare.shared.networking.requests.setters;

import com.polimi.childcare.shared.entities.Menu;

public class SetMenuRequest extends SetEntityRequest<Menu>
{
    public SetMenuRequest(Menu entity, boolean toDelete, int oldHashCode) {
        super(entity, toDelete, oldHashCode);
    }

    public SetMenuRequest(Menu entity, int oldHashCode) {
        super(entity, oldHashCode);
    }

    public SetMenuRequest(Menu entity) {
        super(entity);
    }
}
