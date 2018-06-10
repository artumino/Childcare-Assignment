package com.polimi.childcare.client.ui.controls;

import com.polimi.childcare.client.ui.controls.events.IElementDragDropEvent;
import com.polimi.childcare.shared.serialization.SerializationUtils;
import javafx.beans.DefaultProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.input.*;
import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;

/**
 * Classe utile per la creazione di TableView dinamiche che funzionano tramite Drag&amp;Drop
 * @param <T> Tipo di elementi che gestisce la tabella
 */
@DefaultProperty("items")
public class DragAndDropTableView<T extends Serializable> extends TableView<T>
{
    private ArrayList<T> startingTableViewItems;
    private Class<? extends T> acceptedClasses;
    private boolean deleteOnExit;
    private IntegerProperty maximumRows = new SimpleIntegerProperty();
    private IElementDragDropEvent<T> onElementDropped;
    private IElementDragDropEvent<T> onElementDeleted;

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
        maximumRowsProperty().setValue(null);
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
            if(acceptedClasses != null && event.getGestureSource() == this)
            {
                Dragboard db = event.getDragboard();
                if (db.hasString()) {
                    String dataString = db.getString();
                    byte[] data = Base64.getDecoder().decode(dataString);
                    T element = SerializationUtils.deserializeByteArray(data, acceptedClasses);


                    if (deleteOnExit && element != null && element.equals(getSelectionModel().getSelectedItem()))
                    {
                        if(onElementDeleted != null)
                            onElementDeleted.execute(element, event.getGestureSource(), event.getGestureTarget());
                        else
                            getItems().remove(getSelectionModel().getSelectedItem());
                    }
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
                if (db.hasString() && (maximumRowsProperty().getValue() == null ||
                                        maximumRowsProperty().getValue() == 0 ||
                                        maximumRowsProperty().getValue() > getItems().size()))
                {
                    String dataString = db.getString();
                    byte[] data = Base64.getDecoder().decode(dataString);
                    T element = SerializationUtils.deserializeByteArray(data, acceptedClasses);
                    if (element != null)
                    {

                        if(onElementDropped != null)
                            onElementDropped.execute(element, event.getGestureSource(), event.getGestureTarget());
                        else
                        {
                            if (!this.getItems().contains(element))
                                this.getItems().add(element);
                        }
                        this.refresh();
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

    public void setOnElementDropped(IElementDragDropEvent<T> elementDropEvent)
    {
        this.onElementDropped = elementDropEvent;
    }

    public void setOnElementDeleted(IElementDragDropEvent<T> elementExited)
    {
        this.onElementDeleted = elementExited;
    }

    public void dragForClass(Class<? extends T> tClass)
    {
        this.acceptedClasses = tClass;
    }

    public IntegerProperty maximumRowsProperty() { return maximumRows; }
    public void setMaximumRows(Integer value) { maximumRowsProperty().setValue(value); }
    public Integer getMaximumRows() { return maximumRowsProperty().getValue(); }

    public void setStartingTableViewItems(ArrayList<T> items)
    {
        CollectionUtils.addAll(startingTableViewItems, items);
    }

    public void reset()
    {
        this.setItems(FXCollections.observableArrayList(startingTableViewItems));
    }
}
