package com.polimi.childcare.client.ui.filters;

import com.polimi.childcare.client.ui.utils.DateUtils;
import com.polimi.childcare.shared.entities.Persona;

public class PersonaFilters
{
    public static boolean filterPersona(Persona persona, String query)
    {
        String lowerCaseFilter = query.toLowerCase();
        boolean spaced = lowerCaseFilter.contains(" ");

        try
        {
            //Se è una matricola
            Integer.parseInt(lowerCaseFilter);
            return String.valueOf(persona.getID()).contains(lowerCaseFilter);
        }
        catch (Exception ex)
        {
            //Se è qualcos'altro
            if(persona.getNome().toLowerCase().contains(lowerCaseFilter))
                return true;
            if(persona.getCognome().toLowerCase().contains(lowerCaseFilter))
                return true;
            if(DateUtils.dateToShortString(persona.getDataNascita()).contains(lowerCaseFilter))
                return true;
            if(persona.getCodiceFiscale().toLowerCase().contains(lowerCaseFilter))
                return true;
            return spaced && ((persona.getNome().toLowerCase() + " " + persona.getCognome().toLowerCase()).contains(lowerCaseFilter)
                    || (persona.getCognome().toLowerCase() + " " + persona.getNome().toLowerCase()).contains(lowerCaseFilter));
        }
    }
}
