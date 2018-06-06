package com.polimi.childcare.client.ui.filters;

import com.polimi.childcare.client.ui.utils.DateUtils;
import com.polimi.childcare.client.ui.utils.TableUtils;
import com.polimi.childcare.shared.entities.*;

/**
 * Filtri comuni da applicare alle entità (Utili per filtrare liste, etc. con una sola query string)
 */
public class Filters
{
    public static boolean filterPersona(Persona persona, String query)
    {
        String lowerCaseFilter = query.toLowerCase().trim();
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
            if(persona.getNome().toLowerCase().trim().contains(lowerCaseFilter))
                return true;
            if(persona.getCognome().toLowerCase().trim().contains(lowerCaseFilter))
                return true;
            if(DateUtils.dateToShortString(persona.getDataNascita()).contains(lowerCaseFilter))
                return true;
            if(persona.getCodiceFiscale().toLowerCase().trim().contains(lowerCaseFilter))
                return true;
            return spaced && ((persona.getNome().toLowerCase().trim() + " " + persona.getCognome().toLowerCase().trim()).contains(lowerCaseFilter)
                    || (persona.getCognome().toLowerCase().trim() + " " + persona.getNome().toLowerCase().trim()).contains(lowerCaseFilter));
        }
    }

    public static boolean filterFornitore(Fornitore fornitore, String query)
    {
        String lowerCaseFilter = query.toLowerCase().trim();

        //Se è qualcos'altro
        if(fornitore.getRagioneSociale().toLowerCase().trim().contains(lowerCaseFilter))
            return true;
        if(fornitore.getEmail().toLowerCase().trim().contains(lowerCaseFilter))
            return true;
        if(fornitore.getIBAN().toLowerCase().trim().contains(lowerCaseFilter))
            return true;
        if(fornitore.getNumeroRegistroImprese().toLowerCase().trim().contains(lowerCaseFilter))
            return true;
        if(fornitore.getPartitaIVA().toLowerCase().trim().contains(lowerCaseFilter))
            return true;
        if(TableUtils.iterableToString(fornitore.getTelefoni()).toLowerCase().trim().contains(lowerCaseFilter))
            return true;
        return TableUtils.iterableToString(fornitore.getFax()).toLowerCase().trim().contains(lowerCaseFilter);
    }

    public static boolean filterContatto(Contatto contatto, String query)
    {
        String lowerCaseFilter = query.toLowerCase().trim();
        boolean spaced = lowerCaseFilter.contains(" ");

        //Se è qualcos'altro
        if(contatto.getNome().toLowerCase().trim().contains(lowerCaseFilter))
            return true;
        if(contatto.getCognome().toLowerCase().trim().contains(lowerCaseFilter))
            return true;
        if(contatto.getIndirizzo().toLowerCase().trim().contains(lowerCaseFilter))
            return true;
        if(TableUtils.iterableToString(contatto.getTelefoni()).toLowerCase().trim().contains(lowerCaseFilter))
            return true;
        return spaced && ((contatto.getNome().toLowerCase().trim() + " " + contatto.getCognome().toLowerCase().trim()).contains(lowerCaseFilter)
                || (contatto.getCognome().toLowerCase().trim() + " " + contatto.getNome().toLowerCase().trim()).contains(lowerCaseFilter));
    }

    public static boolean filterPasto(Pasto pasto, String query)
    {
        String lowerCaseFilter = query.toLowerCase().trim();

        if(pasto.getNome().toLowerCase().trim().contains(lowerCaseFilter))
            return true;
        return pasto.getFornitore() != null && pasto.getFornitore().getRagioneSociale().toLowerCase().trim().contains(lowerCaseFilter);
    }

    public static boolean filterMenu(Menu menu, String query)
    {
        String lowerCaseFilter = query.toLowerCase().trim();

        return menu.getNome().toLowerCase().trim().contains(lowerCaseFilter);
    }
}
