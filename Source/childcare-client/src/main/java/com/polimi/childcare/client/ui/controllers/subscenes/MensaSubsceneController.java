package com.polimi.childcare.client.ui.controllers.subscenes;

import com.polimi.childcare.client.ui.OrderedFilteredList;
import com.polimi.childcare.client.ui.components.FilterComponent;
import com.polimi.childcare.client.ui.controllers.ISceneController;
import com.polimi.childcare.client.ui.controllers.ISubSceneController;
import com.polimi.childcare.shared.entities.Menu;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.util.Callback;

public class MensaSubsceneController implements ISubSceneController
{
    public static final String FXML_PATH = "";

    private Parent root;

    @FXML private AnchorPane rootPane;
    @FXML private TableView<Menu> tableMenu;
    @FXML private Button btnRefresh;
    @FXML private TextField txtFilterMenu;
    @FXML private TextField txtFilterPasti;
    @FXML private Button btnAddMenu;
    @FXML private Button btnAddPasto;


    //Generated
    private FilterComponent<Menu> filterMenu;
    private OrderedFilteredList<Menu> listMenu;

    @FXML
    protected void initialize()
    {
        setupMenuTable();


    }

    @Override
    public void attached(ISceneController sceneController, Object... args)
    {

    }

    @Override
    public void detached()
    {

    }

    private void setupMenuTable()
    {
        TableColumn<Menu, String> cMenuName = new TableColumn<>();
        TableColumn<Menu, Boolean> cRicLun = new TableColumn<>();
        TableColumn<Menu, Boolean> cRicMar = new TableColumn<>();
        TableColumn<Menu, Boolean> cRicMer = new TableColumn<>();
        TableColumn<Menu, Boolean> cRicGio = new TableColumn<>();
        TableColumn<Menu, Boolean> cRicVen = new TableColumn<>();
        TableColumn<Menu, Boolean> cRicSab = new TableColumn<>();
        TableColumn<Menu, Boolean> cRicDom = new TableColumn<>();

        cMenuName.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getNome()));

        cRicLun.setCellFactory(menuIntegerTableColumn -> new CheckBoxTableCell<>());
        cRicMar.setCellFactory(menuIntegerTableColumn -> new CheckBoxTableCell<>());
        cRicMer.setCellFactory(menuIntegerTableColumn -> new CheckBoxTableCell<>());
        cRicGio.setCellFactory(menuIntegerTableColumn -> new CheckBoxTableCell<>());
        cRicVen.setCellFactory(menuIntegerTableColumn -> new CheckBoxTableCell<>());
        cRicSab.setCellFactory(menuIntegerTableColumn -> new CheckBoxTableCell<>());
        cRicDom.setCellFactory(menuIntegerTableColumn -> new CheckBoxTableCell<>());

        cRicLun.setCellValueFactory(p -> new ReadOnlyBooleanWrapper(p.getValue().isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Lun)));
        cRicMar.setCellValueFactory(p -> new ReadOnlyBooleanWrapper(p.getValue().isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Mar)));
        cRicMer.setCellValueFactory(p -> new ReadOnlyBooleanWrapper(p.getValue().isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Mer)));
        cRicGio.setCellValueFactory(p -> new ReadOnlyBooleanWrapper(p.getValue().isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Gio)));
        cRicVen.setCellValueFactory(p -> new ReadOnlyBooleanWrapper(p.getValue().isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Ven)));
        cRicSab.setCellValueFactory(p -> new ReadOnlyBooleanWrapper(p.getValue().isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Sab)));
        cRicDom.setCellValueFactory(p -> new ReadOnlyBooleanWrapper(p.getValue().isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Dom)));

        tableMenu.getColumns().addAll(cMenuName, cRicLun, cRicMar, cRicMer, cRicGio, cRicVen, cRicSab, cRicDom);
    }

    void OnMenuResponseRecived(BaseResponse response)
    {

    }

    void OnPastiResponseRecived(BaseResponse response)
    {

    }

    private void redrawData()
    {

    }


    @Override
    public Region getSceneRegion() {
        return this.rootPane;
    }

    @Override
    public Parent getRoot()
    {
        return this.root;
    }

    @Override
    public Scene setupScene(Parent parent)
    {
        this.root = parent;
        return root.getScene();
    }
}
