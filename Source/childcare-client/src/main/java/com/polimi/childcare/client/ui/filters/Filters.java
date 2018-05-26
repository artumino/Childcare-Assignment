package com.polimi.childcare.client.ui.filters;

import com.polimi.childcare.client.ui.utils.DateUtils;
import com.polimi.childcare.client.ui.utils.TableUtils;
import com.polimi.childcare.shared.entities.Contatto;
import com.polimi.childcare.shared.entities.Fornitore;
import com.polimi.childcare.shared.entities.Persona;

/**
 * Filtri comuni da applicare alle entità (Utili per filtrare liste, etc. con una sola query string)
 */
public class Filters
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

    public static boolean filterFornitore(Fornitore fornitore, String query)
    {
        String lowerCaseFilter = query.toLowerCase();

        //Se è qualcos'altro
        if(fornitore.getRagioneSociale().toLowerCase().contains(lowerCaseFilter))
            return true;
        if(fornitore.getEmail().toLowerCase().contains(lowerCaseFilter))
            return true;
        if(fornitore.getIBAN().toLowerCase().contains(lowerCaseFilter))
            return true;
        if(fornitore.getNumeroRegistroImprese().toLowerCase().contains(lowerCaseFilter))
            return true;
        if(fornitore.getPartitaIVA().toLowerCase().contains(lowerCaseFilter))
            return true;
        if(TableUtils.iterableToString(fornitore.getTelefoni()).toLowerCase().contains(lowerCaseFilter))
            return true;
        return TableUtils.iterableToString(fornitore.getFax()).toLowerCase().contains(lowerCaseFilter);
    }

    public static boolean filterContatto(Contatto contatto, String query)
    {
        String lowerCaseFilter = query.toLowerCase();
        boolean spaced = lowerCaseFilter.contains(" ");

        //Se è qualcos'altro
        if(contatto.getNome().toLowerCase().contains(lowerCaseFilter))
            return true;
        if(contatto.getCognome().toLowerCase().contains(lowerCaseFilter))
            return true;
        if(contatto.getIndirizzo().toLowerCase().contains(lowerCaseFilter))
            return true;
        if(TableUtils.iterableToString(contatto.getTelefoni()).toLowerCase().contains(lowerCaseFilter))
            return true;
        return spaced && ((contatto.getNome().toLowerCase() + " " + contatto.getCognome().toLowerCase()).contains(lowerCaseFilter)
                || (contatto.getCognome().toLowerCase() + " " + contatto.getNome().toLowerCase()).contains(lowerCaseFilter));
    }
}
