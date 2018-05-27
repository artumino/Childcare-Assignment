package com.polimi.childcare.client.ui.utils;

import javafx.scene.layout.Pane;

import javax.swing.text.TableView;

public class TableUtils
{
    /**
     * Data un iterable restituisce una rappresentazione in stringa con formattazione opportuna per l'utilizzo in una cella
     * in tabella
     * @param iterable Elementi da trasformare in String
     * @param <T> Tipo di elemento contenuto in list
     * @return String formattata opportunamente
     */
    public static <T> String iterableToString(Iterable<T> iterable)
    {
        String str = "";

        if(iterable != null)
            for (T element : iterable)
                str += element.toString() + "\r\n";

        return str;
    }
}
