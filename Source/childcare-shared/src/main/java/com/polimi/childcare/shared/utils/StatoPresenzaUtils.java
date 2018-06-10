package com.polimi.childcare.shared.utils;

import com.polimi.childcare.shared.entities.RegistroPresenze;

public class StatoPresenzaUtils
{
    public static RegistroPresenze.StatoPresenza getSuggestedStatoPresenzaFromCurrentStato(RegistroPresenze.StatoPresenza currentStato)
    {
        switch (currentStato)
        {
            case Presente:
                return RegistroPresenze.StatoPresenza.UscitoInAnticipo;
            case EntratoInRitardo:
                return RegistroPresenze.StatoPresenza.UscitoInAnticipo;
        }
        return RegistroPresenze.StatoPresenza.Presente;
    }
}
