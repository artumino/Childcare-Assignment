package com.polimi.childcare.shared.utils;

import com.polimi.childcare.shared.entities.RegistroPresenze;

import java.time.LocalDateTime;
import java.time.LocalTime;

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

    public static RegistroPresenze.StatoPresenza getSuggestedStatoPresenzaFromPresenza(RegistroPresenze currentPresenza, boolean isUscita)
    {
        return getSuggestedStatoPresenzaFromPresenza(currentPresenza, isUscita, LocalTime.of(18, 0), LocalTime.of(7, 30));
    }

    public static RegistroPresenze.StatoPresenza getSuggestedStatoPresenzaFromPresenza(RegistroPresenze currentPresenza, boolean isUscita, LocalTime instant)
    {
        return getSuggestedStatoPresenzaFromPresenza(currentPresenza, isUscita, instant, LocalTime.of(18, 0), LocalTime.of(7, 30));
    }

    public static RegistroPresenze.StatoPresenza getSuggestedStatoPresenzaFromPresenza(RegistroPresenze currentPresenza, boolean isUscita, LocalTime endGiornata, LocalTime startGiornata)
    {
        return getSuggestedStatoPresenzaFromPresenza(currentPresenza, isUscita, LocalTime.now(), endGiornata, startGiornata);
    }

    public static RegistroPresenze.StatoPresenza getSuggestedStatoPresenzaFromPresenza(RegistroPresenze currentPresenza, boolean isUscita, LocalTime instant, LocalTime endGiornata, LocalTime startGiornata)
    {
        switch (currentPresenza.getStato())
        {
            case Presente:
            case EntratoInRitardo:
                if(isUscita)
                    if(instant.isBefore(endGiornata))
                        return RegistroPresenze.StatoPresenza.UscitoInAnticipo;
                    else
                        return RegistroPresenze.StatoPresenza.Uscito;
                else
                    return null;

            case Assente:
            case Uscito:
            case UscitoInAnticipo:
                if(isUscita)
                    return null;
                else
                if(instant.isBefore(startGiornata))
                    return RegistroPresenze.StatoPresenza.Presente;
                else if(instant.isBefore(endGiornata))
                    return RegistroPresenze.StatoPresenza.EntratoInRitardo;

            case Disperso:
                if(isUscita)
                    return RegistroPresenze.StatoPresenza.Uscito;
                else
                    return RegistroPresenze.StatoPresenza.Presente;

            case PresenteMezzoErrato:
                if(isUscita)
                    if(instant.isBefore(endGiornata))
                        return RegistroPresenze.StatoPresenza.UscitoInAnticipo;
                    else
                        return RegistroPresenze.StatoPresenza.Uscito;
                else
                    return RegistroPresenze.StatoPresenza.Presente;
        }
        return RegistroPresenze.StatoPresenza.Presente;
    }
}
