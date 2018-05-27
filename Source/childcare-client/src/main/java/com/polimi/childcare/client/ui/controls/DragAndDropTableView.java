package com.polimi.childcare.client.ui.controls;

import com.polimi.childcare.shared.serialization.SerializationUtils;
import javafx.beans.DefaultProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;

/**
 * Classe utile per la creazione di TableView dinamiche che funzionano tramite Drag&Drop
 * @param <T> Tipo di elementi che gestisce la tabella
 */
@DefaultProperty("items")
public class DragAndDropTableView<T extends Serializable> extends TableView<T>
{
    private ArrayList<T> startingTableViewItems;
    private Class<T> acceptedClasses;
    private boolean deleteOnExit;

    public DragAndDropTableView()
    {
        super();
        dragInit(null);
    }

    public DragAndDropTableView(ObservableList<T> items)
    {
        super(items);
        dragInit(items);
    }

    private void dragInit(ObservableList<T> items)
    {
        startingTableViewItems = new ArrayList<>();
        deleteOnExit = true;

        if(items != null)
            CollectionUtils.addAll(startingTableViewItems, items);

        //Notifica le altre tabelle che si sta trascinando un elemento
        setOnDragDetected((event -> {
            T selectedObject = getSelectionModel().getSelectedItem();

            if(selectedObject != null)
            {
                Dragboard dragBoard = startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.putString(Base64.getEncoder().encodeToString(SerializationUtils.serializeToByteArray(selectedObject)));
                dragBoard.setContent(content);
                event.consume();
            }
        }));

        //Rimuove elementi trascinati fuori
        setOnDragExited((event -> {
            if(acceptedClasses != null && deleteOnExit)
            {
                Dragboard db = event.getDragboard();
                if (db.hasString()) {
                    String dataString = db.getString();
                    byte[] data = Base64.getDecoder().decode(dataString);
                    T element = SerializationUtils.deserializeByteArray(data, acceptedClasses);
                    if (element != null && element.equals(getSelectionModel().getSelectedItem()))
                        getItems().remove(getSelectionModel().getSelectedItem());
                }
            }
        }));

        //Accetto tutte le stringhe
        setOnDragOver(event -> {
            if(acceptedClasses != null)
            {
                if (event.getDragboard().hasString()) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            }
        });

        //Aggiunge elementi trascinati dentro
        setOnDragDropped((event -> {
            if(acceptedClasses != null)
            {
                Dragboard db = event.getDragboard();
                if (db.hasString()) {
                    String dataString = db.getString();
                    byte[] data = Base64.getDecoder().decode(dataString);
                    T element = SerializationUtils.deserializeByteArray(data, acceptedClasses);
                    if (element != null) {
                        if (!this.getItems().contains(element))
                            this.getItems().add(element);
                        event.setDropCompleted(true);
                    }
                }
                event.consume();
            }
        }));
    }

    public void setDeleteOnExit(boolean deleteOnExit)
    {
        this.deleteOnExit = deleteOnExit;
    }

    public void dragForClass(Class<T> tClass)
    {
        this.acceptedClasses = tClass;
    }

    public void setStartingTableViewItems(ArrayList<T> items)
    {
        CollectionUtils.addAll(startingTableViewItems, items);
    }

    public void reset()
    {
        this.setItems(FXCollections.observableArrayList(startingTableViewItems));
    }
}
