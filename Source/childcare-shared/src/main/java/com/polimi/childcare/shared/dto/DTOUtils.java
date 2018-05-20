package com.polimi.childcare.shared.dto;

import com.polimi.childcare.shared.entities.ITransferable;

public class DTOUtils
{
    public static <T extends ITransferable> void iterableToDTO(Iterable<T> iterable)
    {
        for(T proxy : iterable)
            if(!proxy.isDTO())
                proxy.toDTO();
    }
}
