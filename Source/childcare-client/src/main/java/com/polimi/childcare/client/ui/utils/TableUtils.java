package com.polimi.childcare.client.ui.utils;

import com.jfoenix.controls.JFXButton;
import com.polimi.childcare.client.ui.controls.events.IElementDragDropEvent;
import com.polimi.childcare.client.ui.utils.interfaces.IBambinoPresenzaSetClicked;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.entities.RegistroPresenze;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.input.MouseEvent;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

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
        StringBuilder str = new StringBuilder();

        if(iterable != null)
            for (T element : iterable)
                str.append(element.toString()).append("\r\n");

        return str.toString();
    }


    public static TableCell<Bambino, RegistroPresenze.StatoPresenza> createPresenzaTableCell(SimpleObjectProperty<Map<Bambino, RegistroPresenze>> statoPresenze, IBambinoPresenzaSetClicked eventCallback)
    {
        return new TableCell<Bambino, RegistroPresenze.StatoPresenza>()
        {
            @Override
            protected void updateItem(RegistroPresenze.StatoPresenza item, boolean empty)
            {
                if(!empty)
                {
                    Bambino bambino = (Bambino) getTableRow().getItem();
                    RegistroPresenze statoPresenza = statoPresenze.get().get(bambino);
                    RegistroPresenze.StatoPresenza statoCorrente = ((statoPresenza != null) ?
                            (statoPresenza.getTimeStamp().toInstant(ZoneOffset.UTC).toEpochMilli() >= LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()) ?
                                    statoPresenza.getStato() :
                                    RegistroPresenze.StatoPresenza.Assente
                            : RegistroPresenze.StatoPresenza.Assente);

                    Button graphicButton = new JFXButton(statoCorrente.name());
                    graphicButton.setOnMouseClicked(p -> eventCallback.OnBambinoPresenzaSetClicked(bambino));

                    graphicButton.setStyle("-fx-background-color: #f00;");
                    switch (statoCorrente) {
                        case Presente:
                            graphicButton.setStyle("-fx-background-color: #0f0;");
                            break;
                        case PresenteMezzoErrato:
                            graphicButton.setStyle("-fx-background-color: DarkOrange;");
                            break;
                        case EntratoInRitardo:
                            graphicButton.setStyle("-fx-background-color: #ff0;");
                            break;
                        case UscitoInAnticipo:
                            graphicButton.setStyle("-fx-background-color: #00f;");
                            break;
                    }
                    graphicButton.setStyle(graphicButton.getStyle() + "-fx-text-fill: #fff;");
                    setGraphic(graphicButton);
                }
                else
                    setGraphic(null);
            }
        };
    }
}
