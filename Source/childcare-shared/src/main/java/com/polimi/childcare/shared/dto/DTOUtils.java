package com.polimi.childcare.shared.dto;

import com.polimi.childcare.shared.entities.TransferableEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DTOUtils
{
    public static <T extends TransferableEntity, IT extends Iterable<T>> IT iterableToDTO(IT iterable)
    {
        try {
            if(iterable == null)
                return null;

            for (T proxy : iterable)
                objectToDTO(proxy);
            return iterable;
        }
        catch (Exception ignored)
        {
            //Ignored
        }
        return null;
    }

    public static <T extends TransferableEntity> T objectToDTO(T object)
    {
        if(object != null)
        {
            try {
                if(!object.isDTO())
                    object.toDTO();
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
