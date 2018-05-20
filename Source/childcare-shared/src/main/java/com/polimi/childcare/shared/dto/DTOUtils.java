package com.polimi.childcare.shared.dto;

import com.polimi.childcare.shared.entities.ITransferable;

public class DTOUtils
{
    public static <T extends ITransferable> void iterableToDTO(Iterable<T> iterable)
    {
        if(iterable == null)
            return;

        for(T proxy : iterable)
            objectToDTO(proxy);
    }

    public static <T extends ITransferable> void objectToDTO(T object)
    {
        if(object != null && !object.isDTO())
            object.toDTO();
    }

    public static <T extends ITransferable> boolean isDTO(T object)
    {
        return object != null && object.isDTO();
    }
}
