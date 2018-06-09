package com.polimi.childcare.client.ui.controls.events;

import java.io.Serializable;

public interface IElementDragDropEvent<T extends Serializable>
{
    void execute(T element, Object source, Object target);
}
