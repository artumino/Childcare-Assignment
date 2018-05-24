package com.polimi.childcare.shared.utils;

import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.entities.RegistroPresenze;

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
                if(!mappaPresenze.containsKey(bambino) || mappaPresenze.get(bambino).getTimeStamp().before(presenza.getTimeStamp()))
                    mappaPresenze.put(bambino, presenza);
            }
        }

        return mappaPresenze;
    }
}
