package com.polimi.childcare.client.ui.controllers.subscenes;

import com.polimi.childcare.client.shared.networking.NetworkOperation;
import com.polimi.childcare.client.ui.OrderedFilteredList;
import com.polimi.childcare.client.ui.components.FilterComponent;
import com.polimi.childcare.client.ui.controllers.BaseStageController;
import com.polimi.childcare.client.ui.controllers.ChildcareBaseStageController;
import com.polimi.childcare.client.ui.controllers.ISceneController;
import com.polimi.childcare.client.ui.controllers.ISubSceneController;
import com.polimi.childcare.client.ui.controls.GruppoContainerComponent;
import com.polimi.childcare.client.ui.filters.Filters;
import com.polimi.childcare.client.ui.utils.StageUtils;
import com.polimi.childcare.client.ui.utils.TableUtils;
import com.polimi.childcare.shared.entities.*;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredBambiniRequest;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredGruppoRequest;
import com.polimi.childcare.shared.networking.requests.special.FilteredLastPresenzaRequest;
import com.polimi.childcare.shared.networking.requests.special.GetCurrentGitaRequest;
import com.polimi.childcare.shared.networking.requests.special.StartPresenzaCheckRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListBambiniResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListGitaResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListGruppoResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListRegistroPresenzeResponse;
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
    @FXML private TableView<Pasto> tableGite;
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
