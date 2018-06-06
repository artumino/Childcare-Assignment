package com.polimi.childcare.client.ui.controllers.subscenes;

import com.polimi.childcare.client.shared.networking.NetworkOperation;
import com.polimi.childcare.client.ui.OrderedFilteredList;
import com.polimi.childcare.client.ui.components.FilterComponent;
import com.polimi.childcare.client.ui.controllers.ISceneController;
import com.polimi.childcare.client.ui.controllers.ISubSceneController;
import com.polimi.childcare.client.ui.utils.StageUtils;
import com.polimi.childcare.shared.entities.Menu;
import com.polimi.childcare.shared.entities.Pasto;
import com.polimi.childcare.shared.networking.requests.BaseRequest;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredMenuRequest;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredPastoRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListMenuResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListPastiResponse;
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

import java.util.HashMap;

public class MensaSubsceneController extends NetworkedSubScene implements ISubSceneController
{
    public static final String FXML_PATH = "fxml/MensaScene.fxml";

    private Parent root;

    @FXML private AnchorPane rootPane;
    @FXML private TableView<Menu> tableMenu;
    @FXML private TableView<Pasto> tablePasti;
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
        setupPastiTable();

    }

    @Override
    public void attached(ISceneController sceneController, Object... args)
    {
        redrawData();
    }

    private void setupMenuTable()
    {
        TableColumn<Menu, String> cMenuName = new TableColumn<>("Nome");
        TableColumn<Menu, Boolean> cRicLun = new TableColumn<>("Lun");
        TableColumn<Menu, Boolean> cRicMar = new TableColumn<>("Mar");
        TableColumn<Menu, Boolean> cRicMer = new TableColumn<>("Mer");
        TableColumn<Menu, Boolean> cRicGio = new TableColumn<>("Gio");
        TableColumn<Menu, Boolean> cRicVen = new TableColumn<>("Ven");
        TableColumn<Menu, Boolean> cRicSab = new TableColumn<>("Sab");
        TableColumn<Menu, Boolean> cRicDom = new TableColumn<>("Dom");

        cMenuName.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getNome()));

        cRicLun.setCellFactory(menuIntegerTableColumn -> new CheckBoxTableCell<>());
        cRicMar.setCellFactory(menuIntegerTableColumn -> new CheckBoxTableCell<>());
        cRicMer.setCellFactory(menuIntegerTableColumn -> new CheckBoxTableCell<>());
        cRicGio.setCellFactory(menuIntegerTableColumn -> new CheckBoxTableCell<>());
        cRicVen.setCellFactory(menuIntegerTableColumn -> new CheckBoxTableCell<>());
        cRicSab.setCellFactory(menuIntegerTableColumn -> new CheckBoxTableCell<>());
        cRicDom.setCellFactory(menuIntegerTableColumn -> new CheckBoxTableCell<>());

        cRicLun.setMaxWidth(45);
        cRicLun.setMinWidth(45);
        cRicMar.setMaxWidth(45);
        cRicMar.setMinWidth(45);
        cRicMer.setMaxWidth(45);
        cRicMer.setMinWidth(45);
        cRicGio.setMaxWidth(45);
        cRicGio.setMinWidth(45);
        cRicVen.setMaxWidth(45);
        cRicVen.setMinWidth(45);
        cRicSab.setMaxWidth(45);
        cRicSab.setMinWidth(45);
        cRicDom.setMaxWidth(45);
        cRicDom.setMinWidth(45);

        cRicLun.setCellValueFactory(p -> new ReadOnlyBooleanWrapper(p.getValue().isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Lun)));
        cRicMar.setCellValueFactory(p -> new ReadOnlyBooleanWrapper(p.getValue().isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Mar)));
        cRicMer.setCellValueFactory(p -> new ReadOnlyBooleanWrapper(p.getValue().isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Mer)));
        cRicGio.setCellValueFactory(p -> new ReadOnlyBooleanWrapper(p.getValue().isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Gio)));
        cRicVen.setCellValueFactory(p -> new ReadOnlyBooleanWrapper(p.getValue().isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Ven)));
        cRicSab.setCellValueFactory(p -> new ReadOnlyBooleanWrapper(p.getValue().isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Sab)));
        cRicDom.setCellValueFactory(p -> new ReadOnlyBooleanWrapper(p.getValue().isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Dom)));

        tableMenu.getColumns().addAll(cMenuName, cRicLun, cRicMar, cRicMer, cRicGio, cRicVen, cRicSab, cRicDom);

        tableMenu.getItems().add(new Menu("Pietanza di Mare", true, Menu.DayOfWeekFlag.Lun.getFlag() | Menu.DayOfWeekFlag.Sab.getFlag()));
    }

    private void setupPastiTable()
    {
        TableColumn<Pasto, String> cPastoName = new TableColumn<>("Nome");
        TableColumn<Pasto, String> cPastoFornitore = new TableColumn<>("Fornitore");

        cPastoName.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getNome()));
        cPastoFornitore.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getFornitore() != null ? p.getValue().getFornitore().getRagioneSociale() : ""));

        tablePasti.getColumns().addAll(cPastoName, cPastoFornitore);
    }

    void OnMenuResponseRecived(BaseResponse response)
    {
        networkOperationVault.operationDone(FilteredMenuRequest.class);

        if(StageUtils.HandleResponseError(response, "Errore sconosciuto durante la ricezione dei pasti", p -> response instanceof ListMenuResponse))
            return;

    }

    void OnPastiResponseRecived(BaseResponse response)
    {
        networkOperationVault.operationDone(FilteredPastoRequest.class);

        if(StageUtils.HandleResponseError(response, "Errore sconosciuto durante la ricezione dei pasti", p -> response instanceof ListPastiResponse))
            return;

        tablePasti.getItems().addAll(((ListPastiResponse)response).getPayload());
    }

    private void redrawData()
    {
        networkOperationVault.submitOperation(new NetworkOperation(new FilteredMenuRequest(0,0,false),
                this::OnMenuResponseRecived,
                true));

        networkOperationVault.submitOperation(new NetworkOperation(new FilteredPastoRequest(0,0,true),
                this::OnPastiResponseRecived,
                true));
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
