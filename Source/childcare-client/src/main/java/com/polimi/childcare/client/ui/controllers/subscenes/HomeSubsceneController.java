package com.polimi.childcare.client.ui.controllers.subscenes;

import com.polimi.childcare.client.shared.networking.NetworkOperation;
import com.polimi.childcare.client.ui.OrderedFilteredList;
import com.polimi.childcare.client.ui.components.FilterComponent;
import com.polimi.childcare.client.ui.controllers.BaseStageController;
import com.polimi.childcare.client.ui.controllers.ChildcareBaseStageController;
import com.polimi.childcare.client.ui.controllers.ISceneController;
import com.polimi.childcare.client.ui.controllers.ISubSceneController;
import com.polimi.childcare.client.ui.controllers.stages.mensa.EditMenuStageController;
import com.polimi.childcare.client.ui.controls.GruppoContainerComponent;
import com.polimi.childcare.client.ui.filters.Filters;
import com.polimi.childcare.client.ui.utils.StageUtils;
import com.polimi.childcare.client.ui.utils.TableUtils;
import com.polimi.childcare.shared.entities.*;
import com.polimi.childcare.shared.entities.Menu;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredBambiniRequest;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredGitaRequest;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredGruppoRequest;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredMenuRequest;
import com.polimi.childcare.shared.networking.requests.special.FilteredLastPresenzaRequest;
import com.polimi.childcare.shared.networking.requests.special.GetCurrentGitaRequest;
import com.polimi.childcare.shared.networking.requests.special.StartPresenzaCheckRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.*;
import com.polimi.childcare.shared.utils.EntitiesHelper;
import com.polimi.childcare.shared.utils.StatoPresenzaUtils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;

import java.io.IOException;
import java.time.*;
import java.util.*;

public class HomeSubsceneController extends NetworkedSubScene implements ISubSceneController
{
    public static final String FXML_PATH = "fxml/HomeScene.fxml";

    private Parent root;

    @FXML private AnchorPane rootPane;
    @FXML private Button btnRefresh;

    //Presenze
    @FXML private TableView<Bambino> tablePresenze;
    @FXML private TextField txtFiltro;

    //Menu del Giorno
    @FXML private Text txtNomeMenuDelGiorno;
    @FXML private VBox vboxMenuDelGiorno;
    @FXML private Button btnShowMenuDettagli;
    @FXML private TableView<Pasto> tableMenuDelGiorno;

    //Gite
    private Gita currentGita;
    @FXML private Text txtNomeCurrentGita;
    @FXML private TableView<Gita> tableGite;
    @FXML private SplitPane paneGita;
    @FXML private Button btnRefreshGita;
    @FXML private Button btnCheckPresenze;
    @FXML private HBox hboxGruppi;
    @FXML private TableView<Tappa> tableTappe;
    private List<Gruppo> receivedGroups;
    private List<GruppoContainerComponent> gruppoContainerComponents;

    //Lista persone da visualizzare
    private OrderedFilteredList<Bambino> listaBambini;

    //Mappa presenze
    private Map<Bambino,RegistroPresenze> statoPresenze;

    //Filtri
    private FilterComponent<Bambino> filterBambini;

    @FXML
    protected void initialize()
    {
        listaBambini = new OrderedFilteredList<>();
        filterBambini = new FilterComponent<>(listaBambini.predicateProperty());
        statoPresenze = new HashMap<>();

        //Generated
        gruppoContainerComponents = new ArrayList<>();

        //RefreshDelleListe
        btnRefresh.setOnMouseClicked(event -> {
            RefreshData();
            RefreshWithoutGita();
        });

        btnRefreshGita.setOnMouseClicked(event -> {
            RefreshData();
            RefreshWithGita();
        });

        btnCheckPresenze.setOnMouseClicked(event -> {
            networkOperationVault.submitOperation(new NetworkOperation(new StartPresenzaCheckRequest(currentGita), response -> {
                RefreshData();
                RefreshWithGita();
            }, true));
        });

        setupTablePresenze();
        setupTableGite();
        setupTablePasti();

        filterBambini.addFilterField(txtFiltro.textProperty(), (persona -> Filters.filterPersona(persona, txtFiltro.getText())));

        setupTableTappe();
    }

    @Override
    public void attached(ISceneController sceneController, Object... args)
    {
        //Se sono stato collegato ad uno stage, imposto un titolo opportuno
        if(sceneController instanceof BaseStageController)
            ((BaseStageController)sceneController).requestSetTitle("Home");


        RefreshData();
        RefreshWithoutGita();
    }

    private void setupTablePresenze()
    {
        //Imposto la scena
        TableColumn<Bambino, Integer> id = new TableColumn<>("Matricola");
        TableColumn<Bambino, String> name = new TableColumn<>("Nome");
        TableColumn<Bambino, String> surname = new TableColumn<>("Cognome");
        TableColumn<Bambino, String> fiscalCode = new TableColumn<>("Codice Fiscale");
        TableColumn<Bambino, RegistroPresenze.StatoPresenza> editPresenza = new TableColumn<>("Stato");

        name.setCellValueFactory((cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getNome()));
        surname.setCellValueFactory((cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getCognome()));
        fiscalCode.setCellValueFactory((cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getCodiceFiscale()));

        //Crea Bottoni per Presenza
        editPresenza.setCellFactory((cellData) -> TableUtils.createPresenzaTableCell(new ReadOnlyObjectWrapper<>(statoPresenze), this::OnEditPresenzaBambino));
        editPresenza.setMinWidth(100);
        editPresenza.setPrefWidth(100);
        editPresenza.setMaxWidth(100);

        id.setCellValueFactory((cellData) -> new ReadOnlyObjectWrapper<>(cellData.getValue().getID()));

        if(tablePresenze != null) {
            tablePresenze.getColumns().addAll(id, name, surname, fiscalCode, editPresenza);
            listaBambini.comparatorProperty().bind(tablePresenze.comparatorProperty());
            tablePresenze.setItems(listaBambini.list());
            //tableList.setColumnResizePolicy(p -> true);
        }
    }

    private void setupTableGite()
    {
        TableColumn<Gita, Integer> cID = new TableColumn<>("ID");
        TableColumn<Gita, String> cLuogo = new TableColumn<>("Luogo");
        TableColumn<Gita, LocalDate> cDataInizio = new TableColumn<>("Data Inizio");
        TableColumn<Gita, LocalDate> cDataFine = new TableColumn<>("Data Fine");


        cLuogo.setCellValueFactory((cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getLuogo()));
        cID.setCellValueFactory((cellData) -> new ReadOnlyObjectWrapper<>(cellData.getValue().getID()));
        cDataInizio.setCellValueFactory((cellData) -> new ReadOnlyObjectWrapper<>(cellData.getValue().getDataInizio()));
        cDataFine.setCellValueFactory((cellData) -> new ReadOnlyObjectWrapper<>(cellData.getValue().getDataFine()));

        tableGite.setRowFactory(tv -> new TableRow<Gita>() {
            @Override
            public void updateItem(Gita item, boolean empty) {
                super.updateItem(item, empty) ;
                if (item == null) {
                    setStyle("");
                } else if (item.getDataInizio().atStartOfDay().isBefore(LocalDateTime.now().plusDays(1)) &&
                        item.getDataFine().plusDays(1).atStartOfDay().isAfter(LocalDateTime.now().plusDays(1))) {
                    //Gita attiva
                    setStyle("-fx-background-color: Gold;");
                } else {
                    setStyle("");
                }
            }
        });

        if(tableGite != null)
            tableGite.getColumns().addAll(cID, cLuogo, cDataInizio, cDataFine);
    }

    private void setupTablePasti()
    {
        TableColumn<Pasto, String> cNomePasto = new TableColumn<>("Pasto");


        cNomePasto.setCellValueFactory((cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getNome()));

        if(tableMenuDelGiorno != null)
            tableMenuDelGiorno.getColumns().addAll(cNomePasto);
    }

    private void setupTableTappe()
    {
        //Imposto la scena
        TableColumn<Tappa, String> cLuogo = new TableColumn<>("Luogo");

        cLuogo.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getLuogo()));

        if(tableTappe != null) {
            tableTappe.getColumns().add(cLuogo);
        }
    }

    private void redrawGitaData()
    {
        if(tableTappe != null && currentGita.getTappe() != null)
        {
            tableTappe.getItems().clear();
            tableTappe.getItems().addAll(currentGita.getTappe());
            tableTappe.getItems().sort(Comparator.comparingInt(Tappa::getID));
        }
    }


    private void redrawGruppi()
    {
        if(hboxGruppi != null)
        {
            hboxGruppi.getChildren().clear();
            for (GruppoContainerComponent containerComponent : gruppoContainerComponents)
                hboxGruppi.getChildren().add(containerComponent);
        }
    }

    private void refreshGruppi(List<Gruppo> gruppi)
    {
        receivedGroups = gruppi;
        gruppoContainerComponents.clear();
        for (Gruppo gruppo : gruppi)
            addGruppoComponentForGruppo(gruppo);
        redrawGruppi();
    }

    private void addGruppoComponentForGruppo(Gruppo gruppo)
    {
        GruppoContainerComponent containerComponent = new GruppoContainerComponent(gruppo);
        containerComponent.setDragEnabled(false);

        if(currentGita != null && currentGita.getPianiViaggi() != null)
        {
            MezzoDiTrasporto mezzoDiTrasporto = null;
            for (PianoViaggi pianoViaggi : currentGita.getPianiViaggi())
                if(pianoViaggi.getGruppoForeignKey() == gruppo.getID())
                    mezzoDiTrasporto = pianoViaggi.getMezzo();

            containerComponent.bind(gruppo, mezzoDiTrasporto);
        }

        TableColumn<Bambino, RegistroPresenze.StatoPresenza> cPresenza = new TableColumn<>("Stato");

        cPresenza.setCellFactory((cellData) -> TableUtils.createPresenzaTableCell(new ReadOnlyObjectWrapper<>(statoPresenze), this::OnEditPresenzaBambino));
        cPresenza.setMinWidth(100);
        cPresenza.setPrefWidth(100);
        cPresenza.setMaxWidth(100);

        containerComponent.addBambiniColumn(cPresenza);

        hboxGruppi.getChildren().add(containerComponent);
        gruppoContainerComponents.add(containerComponent);
    }

    private void RefreshWithoutGita()
    {
        networkOperationVault.submitOperation(new NetworkOperation(
                new FilteredBambiniRequest(0, 0, false),
                this::OnBambiniResponseRecived,
                true));

        networkOperationVault.submitOperation(new NetworkOperation(
                new FilteredGitaRequest(0, 0, false),
                this::OnGiteResponseRecived,
                true));

        networkOperationVault.submitOperation(new NetworkOperation(
                new FilteredMenuRequest(0, 0, true),
                this::OnMenuDelGiornoResponse,
                true));
    }

    private void RefreshWithGita()
    {
        networkOperationVault.submitOperation(new NetworkOperation(
                new FilteredGruppoRequest(0, 0, true),
                this::OnGruppoResponse,
                true));
    }

    private void RefreshData()
    {
        //Provo ad aggiornare i dati

        networkOperationVault.submitOperation(new NetworkOperation(
                new FilteredLastPresenzaRequest(0, 0, false),
                this::OnPresenzeRecived,
                true));
        networkOperationVault.submitOperation(new NetworkOperation(
                new FilteredBambiniRequest(0, 0, false),
                this::OnBambiniResponseRecived,
                true));

        networkOperationVault.submitOperation(new NetworkOperation(
                new GetCurrentGitaRequest(LocalDateTime.now().toEpochSecond(ZoneOffset.systemDefault().getRules().getOffset(Instant.now()))),
                this::OnCurrentGitaResponseRecived,
                true));
    }

    private void OnEditPresenzaBambino(Bambino bambino)
    {
        try {
            RegistroPresenze presenzaToEdit = new RegistroPresenze(StatoPresenzaUtils.getSuggestedStatoPresenzaFromCurrentStato(
                    statoPresenze.get(bambino) != null ?
                            statoPresenze.get(bambino).getStato() : RegistroPresenze.StatoPresenza.Assente),
                    LocalDateTime.now().toLocalDate(),
                    LocalDateTime.now(),
                    (short)LocalDateTime.now().getHour(),
                    bambino);

            if(statoPresenze.containsKey(bambino) && statoPresenze.get(bambino).getStato().equals(RegistroPresenze.StatoPresenza.Disperso))
                presenzaToEdit.unsafeSetID(statoPresenze.get(bambino).getID());

            if(currentGita != null)
                presenzaToEdit.setGita(currentGita);

            ChildcareBaseStageController setPresenzeStage = new ChildcareBaseStageController();
            setPresenzeStage.setContentScene(getClass().getClassLoader().getResource("fxml/stages/presenze/SetPresenzaStage.fxml"),
                    presenzaToEdit);
            setPresenzeStage.initModality(Modality.WINDOW_MODAL);
            setPresenzeStage.initOwner(getRoot().getScene().getWindow());
            setPresenzeStage.setOnClosingCallback((returnArgs) -> RefreshData());
            setPresenzeStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void OnBambiniResponseRecived(BaseResponse response)
    {
        networkOperationVault.operationDone(FilteredBambiniRequest.class);
        if(!(response instanceof ListBambiniResponse))
        {
            StageUtils.ShowAlert(Alert.AlertType.ERROR, "Errore nell'aggiornare i dati: " + (response != null ? "Bad Request" : "Risposta Nulla"));
            return;
        }

        ListBambiniResponse bambiniResponse = (ListBambiniResponse)response;

        //Aggiorno i valori
        listaBambini.updateDataSet(bambiniResponse.getPayload());
        tablePresenze.refresh();

    }

    private void OnPresenzeRecived(BaseResponse response)
    {
        networkOperationVault.operationDone(FilteredLastPresenzaRequest.class);
        if(!(response instanceof ListRegistroPresenzeResponse))
        {
            StageUtils.ShowAlert(Alert.AlertType.ERROR, "Errore nell'aggiornare i dati: " + (response != null ? "Bad Request" : "Risposta Nulla"));
            return;
        }

        ListRegistroPresenzeResponse presenzeResponse = (ListRegistroPresenzeResponse)response;

        //Aggiorno i valori
        this.statoPresenze = EntitiesHelper.presenzeToSearchMap(presenzeResponse.getPayload());
        tablePresenze.refresh();
    }

    private void OnGiteResponseRecived(BaseResponse response)
    {
        networkOperationVault.operationDone(FilteredGitaRequest.class);
        if(StageUtils.HandleResponseError(response, "Errore nell'aggiornare i dati", p -> p instanceof ListGitaResponse))
            return;

        ListGitaResponse currentGitaRespose = (ListGitaResponse)response;

        List<Gita> giteToShow = new ArrayList<>();
        for(Gita gita : currentGitaRespose.getPayload())
        {
            if(gita.getDataInizio().isAfter(LocalDate.now()))
                giteToShow.add(gita);
        }

        if(tableGite != null)
        {
            tableGite.getItems().clear();
            tableGite.getItems().addAll(giteToShow);
        }
    }

    private void OnMenuDelGiornoResponse(BaseResponse response)
    {
        networkOperationVault.operationDone(FilteredMenuRequest.class);
        if(StageUtils.HandleResponseError(response, "Errore nell'aggiornare i dati", p -> p instanceof ListMenuResponse))
            return;

        ListMenuResponse currentMenus = (ListMenuResponse)response;

        Menu currentMenu = null;
        for(Menu menu : currentMenus.getPayload())
            if(menu.isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.fromDayOfWeek(DayOfWeek.from(LocalDate.now())))) {
                currentMenu = menu;
                break;
            }

        if(tableMenuDelGiorno != null)
        {
            tableMenuDelGiorno.getItems().clear();

            if(currentMenu != null)
            {
                final Menu finalMenu = currentMenu;
                if (currentMenu.getPasti() != null)
                    tableMenuDelGiorno.getItems().addAll(currentMenu.getPasti());

                txtNomeMenuDelGiorno.setText(currentMenu.getNome());
                btnShowMenuDettagli.setOnMouseClicked(click -> ShowMenuDetails(finalMenu));
            }
            else
            {
                txtNomeMenuDelGiorno.setText("Nessun Menu");
                btnShowMenuDettagli.setOnMouseClicked(null);
            }
        }
    }

    private void OnCurrentGitaResponseRecived(BaseResponse response)
    {
        networkOperationVault.operationDone(GetCurrentGitaRequest.class);
        if(StageUtils.HandleResponseError(response, "Errore nell'aggiornare i dati", p -> p instanceof ListGitaResponse))
            return;

        ListGitaResponse currentGitaRespose = (ListGitaResponse)response;

        if(currentGitaRespose.getPayload().size() > 0)
        {
            currentGita = currentGitaRespose.getPayload().get(0);

            if(!rootPane.getChildren().contains(paneGita))
                rootPane.getChildren().add(paneGita);

            RefreshWithGita();

            txtNomeCurrentGita.setText(currentGita.getLuogo());
            redrawGitaData();
        }
        else
        {
            currentGita = null;
            rootPane.getChildren().remove(paneGita);
            RefreshWithoutGita();
        }
    }

    private void OnGruppoResponse(BaseResponse response)
    {
        networkOperationVault.operationDone(FilteredGruppoRequest.class);

        if(!(response instanceof ListGruppoResponse))
            return;
        ListGruppoResponse gruppoResponse = (ListGruppoResponse)response;

        //Aggiorno i valori
        refreshGruppi(gruppoResponse.getPayload());
    }



    private void ShowMenuDetails(Menu menu)
    {
        try {
            ChildcareBaseStageController setMenuStage = new ChildcareBaseStageController();
            setMenuStage.setContentScene(getClass().getClassLoader().getResource(EditMenuStageController.FXML_PATH), menu);
            setMenuStage.initOwner(getRoot().getScene().getWindow());
            setMenuStage.setOnClosingCallback((returnArgs) -> {
                RefreshData();
            });
            setMenuStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void detached()
    {
        //DO NOTHING...
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
