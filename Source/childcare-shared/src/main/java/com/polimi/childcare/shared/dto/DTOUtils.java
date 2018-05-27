package com.polimi.childcare.shared.dto;

import com.polimi.childcare.shared.entities.TransferableEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DTOUtils
{
    public static <T extends TransferableEntity, IT extends Iterable<T>> IT iterableToDTO(IT iterable, List<Object> processed)
    {
        if(processed == null)
            processed = new ArrayList<>();
        try {
            if(iterable == null)
                return null;

            for (T proxy : iterable)
                objectToDTO(proxy, processed);
            return iterable;
        }
        catch (Exception ignored)
        {
            //Ignored
        }
        return null;
    }

    public static <T extends TransferableEntity> T objectToDTO(T object, List<Object> processed)
    {
        if(processed == null)
            processed = new ArrayList<>();

        if(object != null)
        {
            try {
                if(!object.isDTO() && !processed.contains(object))
                {
                    processed.add(object);
                    object.toDTO(processed);
                }
                return object;
            } catch (Exception ignored)
            {
                //Ignore
            }
        }

        return null;
    }

    public static <T extends TransferableEntity> boolean isDTO(T object)
    {
        try
        {
            return object == null || object.isDTO();
        }
        catch (Exception ignored)
        {

        }
        return false;
    }

    public static <T extends TransferableEntity> boolean isDTO(Set<T> object)
    {
        try
        {
            return object == null || (object instanceof HashSet);
        }
        catch (Exception ignored)
        {

        }
        return false;
    }

    public static <T extends TransferableEntity> boolean isDTO(List<T> object)
    {
        try
        {
            return object == null || (object instanceof ArrayList);
        }
        catch (Exception ignored)
        {

        }
        return false;
    }
}
