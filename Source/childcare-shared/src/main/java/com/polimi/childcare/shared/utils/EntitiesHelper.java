package com.polimi.childcare.shared.utils;

import com.polimi.childcare.shared.entities.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    //Gestione dei telefoni (per evitare relazioni complicate sul DB con il conseguente aumento delle complessità)
    public static List<String> getNumeriTelefonoFromString(String telefoni)
    {
        if(telefoni == null || telefoni.isEmpty())
            return new ArrayList<>();
        return Arrays.asList(telefoni.split("\n"));
    }

    public static String getTelefoniStringFromIterable(Iterable<String> telefoni)
    {
        if(telefoni == null)
            return "";

        StringBuilder returnStr = new StringBuilder();
        for (String telefono : telefoni)
            returnStr.append(telefono.trim()).append("\n");
        return returnStr.toString();
    }

    public static String addTelefonoToString(String telefoni, String newTelefono)
    {
        List<String> telefoniList = getNumeriTelefonoFromString(telefoni);
        newTelefono = newTelefono.trim();
        if(telefoniList.contains(newTelefono))
            return telefoni;

        if(telefoni == null)
            telefoni = "";
        return telefoni.concat(newTelefono + "\n");
    }

    public static String removeTelefonoToString(String telefoni, String telefono)
    {
        List<String> telefoniList = getNumeriTelefonoFromString(telefoni);
        telefono = telefono.trim();
        if(!telefoniList.remove(telefono))
            return telefoni;

        return getTelefoniStringFromIterable(telefoniList);
    }
}
