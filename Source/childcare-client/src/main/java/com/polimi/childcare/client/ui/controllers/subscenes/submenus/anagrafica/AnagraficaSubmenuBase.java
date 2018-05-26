package com.polimi.childcare.client.ui.controllers.subscenes.submenus.anagrafica;

import com.polimi.childcare.client.ui.OrderedFilteredList;
import com.polimi.childcare.client.ui.components.FilterComponent;
import com.polimi.childcare.client.ui.controllers.subscenes.submenus.SubMenuBase;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.Collection;

/**
 * Classe di base per la gestione di un tipico sottomenu della sezione anagrafica (tabella a sinistra e filtri+controlli a destra)
 * @param <T> Tipo di oggetto che verrà visualizzato in tabella
 */
public abstract class AnagraficaSubmenuBase<T> extends SubMenuBase
{
    @FXML protected AnchorPane rootPane;
    @FXML protected TableView<T> tableView;
    @FXML private VBox vboxFilters;
    @FXML private VBox vboxControls;

    //Filter Components
    protected OrderedFilteredList<T> filteredList;
    protected FilterComponent<T> filterComponent;

    //Watchers
    protected T selectedItem;

    protected abstract Collection<TableColumn<T,?>> setupColumns();

    @Override
    /**
     * Sequenza di initialize generica per tutti i componenti di anagrafica
     */
    protected void initialize()
    {
        filteredList = new OrderedFilteredList<>();
        filterComponent = new FilterComponent<>(filteredList.predicateProperty());

        if(tableView != null)
        {
            Collection<TableColumn<T,?>> columnsToShow = setupColumns();
            //Setup delle colonne
            if(columnsToShow != null)
                tableView.getColumns().addAll(columnsToShow);
            filteredList.comparatorProperty().bind(tableView.comparatorProperty());
            tableView.setItems(filteredList.list());

            tableView.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldPersona, newPersona) -> {
                selectedItem = newPersona;
                redrawFilters();
                redrawControls();
            }));
        }

        setupFilterNodes();
        setupControlNodes();
        redrawFilters();
        redrawControls();
    }

    protected abstract void setupFilterNodes();
    protected abstract void setupControlNodes();
    protected abstract Collection<Node> getShownFilterElements();
    protected abstract Collection<Node> getShownControlElements();

    public final void redrawFilters()
    {
        vboxFilters.getChildren().clear();
        Collection<Node> filtersToShow = getShownFilterElements();
        if(filtersToShow != null)
            vboxFilters.getChildren().addAll(filtersToShow);
    }

    public final void redrawControls()
    {
        vboxControls.getChildren().clear();
        Collection<Node> controlsToShow = getShownControlElements();
        if(controlsToShow != null)
            vboxControls.getChildren().addAll(controlsToShow);
    }
}
