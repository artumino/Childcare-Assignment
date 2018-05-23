package com.polimi.childcare.client.ui.controllers.subscenes.submenus.anagrafica;

import com.polimi.childcare.client.ui.controllers.subscenes.submenus.SubMenuBase;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Classe di base per la gestione di un tipico sottomenu della sezione anagrafica (tabella a sinistra e filtri+controlli a destra)
 * @param <T> Tipo di oggetto che verrà visualizzato in tabella
 */
public abstract class AnagraficaSubmenuBase<T> extends SubMenuBase
{
    @FXML protected AnchorPane rootPane;
    @FXML protected TableView<T> tableView;
    @FXML protected VBox vboxFilters;
    @FXML protected VBox vboxControls;
}
