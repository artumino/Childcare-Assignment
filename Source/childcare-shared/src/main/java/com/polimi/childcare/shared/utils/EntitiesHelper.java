package com.polimi.childcare.shared.utils;

import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.entities.RegistroPresenze;

import java.time.LocalDateTime;
import java.util.*;

public class EntitiesHelper
{
    /**
     * Metodo che dato un set ritorna un clone non modificabile
     * @param set Set di elementi da clonare
     * @param <T> Tipo degli elementi nel set
     * @return HashSet di elementi di tipo T non modificabile
     */
    public static <T> Set <T> unmodifiableListReturn(Set<T> set)
    {
        if(set == null)
            return null;

        Set<T> list = new HashSet<>(set);
        Collections.unmodifiableSet(list);
        return list;
    }

    /**
     * Metodo utile per convertire una lista (in generale iterable) di RegistroPresenze inizializzato in una struttura
     * ottimizzata per la ricerca tramite Bambino
     * @param presenze Lista registro presenze
     * @return Lista che associa 1 RegistroPresenze ad 1 Bambino (in caso di più registri per bambini ritorna l'ultimo disponibile)
     */
    public static HashMap<Bambino, RegistroPresenze> presenzeToSearchMap(Iterable<RegistroPresenze> presenze)
    {
        HashMap<Bambino,RegistroPresenze> mappaPresenze = new HashMap<>();

        for(RegistroPresenze presenza : presenze)
        {
            if(presenza.getBambino() != null)
            {
                Bambino bambino = presenza.getBambino();
                if(!mappaPresenze.containsKey(bambino) || mappaPresenze.get(bambino).getTimeStamp().isBefore(presenza.getTimeStamp()))
                    mappaPresenze.put(bambino, presenza);
            }
        }

        return mappaPresenze;
    }


    /**
     * Metodo per cambiare lo stato della presenza
     * @param lista
     * @param dt
     * @param isUscita
     * @return
     * @throws Exception
     */
    public static RegistroPresenze.StatoPresenza presenzeChanger(RegistroPresenze lista, LocalDateTime dt, boolean isUscita) throws Exception
    {
        if(lista.getTimeStamp().isAfter(dt))
            throw new Exception("Formato Errato!");

        else if(lista.getDate().equals(dt.toLocalDate()))
        {
            if(lista.getStato() == RegistroPresenze.StatoPresenza.Presente)
            {
                if (isUscita)
                {
                    if (dt.getHour() > 18)
                        return RegistroPresenze.StatoPresenza.Uscito;
                    else
                        return RegistroPresenze.StatoPresenza.UscitoInAnticipo;
                }
            }
            else if(lista.getStato() == RegistroPresenze.StatoPresenza.Uscito || lista.getStato() == RegistroPresenze.StatoPresenza.UscitoInAnticipo)
                return null;


            else
            {
                if(dt.getHour() < 8)
                    return RegistroPresenze.StatoPresenza.Presente;

                else
                    return RegistroPresenze.StatoPresenza.EntratoInRitardo;
            }
        }

        else
        {
            if(isUscita)
                return null;
            else
            {
                if(dt.getHour() < 8)
                    return RegistroPresenze.StatoPresenza.Presente;

                else
                    return RegistroPresenze.StatoPresenza.EntratoInRitardo;
            }


        }

        return null;
    }

    public static void presenzeChangerRecursive(List<RegistroPresenze> list, RegistroPresenze.StatoPresenza nuovo, LocalDateTime dt, boolean isUscita)
    {
        RegistroPresenze.StatoPresenza st = null;

        for (RegistroPresenze r : list)
        {
            try
            {
                st = presenzeChanger(r, dt, isUscita);

                if(st == null)
                    return;
                
                else
                {
                    int i = list.indexOf(r) - 1;

                    if(st == nuovo)
                    {
                        if(r.getTimeStamp().isAfter(dt))
                        {
                            list.remove(r);

                            if(r.getGita() == null)
                                list.add(i, new RegistroPresenze(st, dt.toLocalDate(), dt, (short)dt.getHour(), r.getBambino()));

                            else
                                list.add(i, new RegistroPresenze(st, dt.toLocalDate(), dt, (short)dt.getHour(), r.getBambino(), r.getGita()));
                        }

                        else
                            return;
                    }

                    else
                    {
                        if(r.getGita() == null)
                            list.add(i, new RegistroPresenze(st, dt.toLocalDate(), dt, (short)dt.getHour(), r.getBambino()));

                        else
                            list.add(i, new RegistroPresenze(st, dt.toLocalDate(), dt, (short)dt.getHour(), r.getBambino(), r.getGita()));

                    }
                }

            }

            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }
}
