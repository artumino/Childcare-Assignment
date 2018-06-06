package com.polimi.childcare.client.ui.controllers.subscenes;

import com.polimi.childcare.client.shared.networking.NetworkOperation;
import com.polimi.childcare.client.ui.OrderedFilteredList;
import com.polimi.childcare.client.ui.components.FilterComponent;
import com.polimi.childcare.client.ui.controllers.ChildcareBaseStageController;
import com.polimi.childcare.client.ui.controllers.ISceneController;
import com.polimi.childcare.client.ui.controllers.ISubSceneController;
import com.polimi.childcare.client.ui.controllers.stages.mensa.EditPastoStageController;
import com.polimi.childcare.client.ui.controls.DragAndDropTableView;
import com.polimi.childcare.client.ui.filters.Filters;
import com.polimi.childcare.client.ui.utils.StageUtils;
import com.polimi.childcare.shared.entities.Menu;
import com.polimi.childcare.shared.entities.Pasto;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredMenuRequest;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredPastoRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListMenuResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListPastiResponse;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.util.Arrays;

public class MensaSubsceneController extends NetworkedSubScene implements ISubSceneController
{
    public static final String FXML_PATH = "fxml/MensaScene.fxml";

    private Parent root;

    @FXML private AnchorPane rootPane;
    @FXML private DragAndDropTableView<Menu> tableMenu;
    @FXML private DragAndDropTableView<Pasto> tablePasti;
    @FXML private Button btnRefresh;
    @FXML private TextField txtFiltroMenu;
    @FXML private TextField txtFiltroPasti;
    @FXML private CheckBox chkShowAttivi;
    @FXML private Button btnAddMenu;
    @FXML private Button btnAddPasto;


    //Generated
    private FilterComponent<Menu> filterMenu;
    private OrderedFilteredList<Menu> listMenu;
    private FilterComponent<Pasto> filterPasti;
    private OrderedFilteredList<Pasto> listPasti;

    @FXML
    protected void initialize()
    {
        listMenu = new OrderedFilteredList<>();
        listPasti = new OrderedFilteredList<>();
        filterMenu = new FilterComponent<>(listMenu.predicateProperty());
        filterPasti = new FilterComponent<>(listPasti.predicateProperty());

        setupMenuTable();
        setupPastiTable();

        filterPasti.addFilterField(txtFiltroPasti.textProperty(), p -> Filters.filterPasto(p, txtFiltroPasti.getText()));
        filterMenu.addFilterField(txtFiltroMenu.textProperty(), m -> Filters.filterMenu(m, txtFiltroMenu.getText()));
        filterMenu.addFilterField(chkShowAttivi.selectedProperty(), m -> !chkShowAttivi.isSelected() || m.getRicorrenza() != 0);

        btnRefresh.setOnMouseClicked(click -> redrawData());
        btnAddPasto.setOnMouseClicked(click -> ShowPastoDetails(new Pasto()));
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

        if(tableMenu != null)
        {

            tableMenu.getColumns().addAll(cMenuName, cRicLun, cRicMar, cRicMer, cRicGio, cRicVen, cRicSab, cRicDom);
            listMenu.comparatorProperty().bind(tableMenu.comparatorProperty());
            tableMenu.setItems(listMenu.list());
        }
    }

    private void setupPastiTable()
    {
        TableColumn<Pasto, String> cPastoName = new TableColumn<>("Nome");
        TableColumn<Pasto, String> cPastoFornitore = new TableColumn<>("Fornitore");

        cPastoName.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getNome()));
        cPastoFornitore.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getFornitore() != null ? p.getValue().getFornitore().getRagioneSociale() : ""));

        if(tablePasti != null)
        {
            tablePasti.setOnMousePressed(click -> {
                if(click.isPrimaryButtonDown() && click.getClickCount() == 2 && tablePasti.getSelectionModel().getSelectedItem() != null)
                    ShowPastoDetails(tablePasti.getSelectionModel().getSelectedItem());
            });

            tablePasti.getColumns().addAll(cPastoName, cPastoFornitore);
            listPasti.comparatorProperty().bind(tablePasti.comparatorProperty());
            tablePasti.setItems(listPasti.list());
        }
    }

    void OnMenuResponseRecived(BaseResponse response)
    {
        networkOperationVault.operationDone(FilteredMenuRequest.class);

        if(StageUtils.HandleResponseError(response, "Errore sconosciuto durante la ricezione dei pasti", p -> p instanceof ListMenuResponse))
            return;

        listMenu.updateDataSet(((ListMenuResponse)response).getPayload());

        //Debug
        listMenu.updateDataSet(Arrays.asList(
                new Menu("Pietanza di Mare", true, Menu.DayOfWeekFlag.Lun.getFlag() | Menu.DayOfWeekFlag.Sab.getFlag()),
                new Menu("Pietanze di Edo", false, 0)));
    }

    void OnPastiResponseRecived(BaseResponse response)
    {
        networkOperationVault.operationDone(FilteredPastoRequest.class);

        if(StageUtils.HandleResponseError(response, "Errore sconosciuto durante la ricezione dei pasti", p -> p instanceof ListPastiResponse))
            return;

        listPasti.updateDataSet(((ListPastiResponse)response).getPayload());
    }

    private void redrawData()
    {
        refreshMenus();
        refreshPasti();
    }

    private void refreshMenus()
    {
        networkOperationVault.submitOperation(new NetworkOperation(new FilteredMenuRequest(0,0,false),
                this::OnMenuResponseRecived,
                true));
    }

    private void refreshPasti()
    {
        networkOperationVault.submitOperation(new NetworkOperation(new FilteredPastoRequest(0,0,false),
                this::OnPastiResponseRecived,
                true));
    }

    private void ShowPastoDetails(Pasto pasto)
    {
        try {
            ChildcareBaseStageController setPastoStage = new ChildcareBaseStageController();
            setPastoStage.setContentScene(getClass().getClassLoader().getResource(EditPastoStageController.FXML_PATH), pasto);
            //setPresenzeStage.initOwner(getRoot().getScene().getWindow());
            setPastoStage.setOnClosingCallback((returnArgs) -> {
                //Niente
            });
            setPastoStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
