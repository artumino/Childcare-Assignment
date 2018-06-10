package com.polimi.childcare.client.ui.controllers.subscenes;

import com.polimi.childcare.client.shared.networking.NetworkOperation;
import com.polimi.childcare.client.ui.OrderedFilteredList;
import com.polimi.childcare.client.ui.components.FilterComponent;
import com.polimi.childcare.client.ui.controllers.BaseStageController;
import com.polimi.childcare.client.ui.controllers.ChildcareBaseStageController;
import com.polimi.childcare.client.ui.controllers.ISceneController;
import com.polimi.childcare.client.ui.controllers.ISubSceneController;
import com.polimi.childcare.client.ui.controllers.stages.gita.EditGitaStageController;
import com.polimi.childcare.client.ui.controllers.stages.gita.EditMezzoStageController;
import com.polimi.childcare.client.ui.controllers.stages.gita.EditPianoViaggiController;
import com.polimi.childcare.client.ui.controls.DragAndDropTableView;
import com.polimi.childcare.client.ui.controls.GruppoContainerComponent;
import com.polimi.childcare.client.ui.filters.Filters;
import com.polimi.childcare.client.ui.utils.EffectsUtils;
import com.polimi.childcare.client.ui.utils.StageUtils;
import com.polimi.childcare.shared.entities.*;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredAddettoRequest;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredGitaRequest;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredGruppoRequest;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredMezzoDiTrasportoRequest;
import com.polimi.childcare.shared.networking.requests.special.GetBambiniSenzaGruppoRequest;
import com.polimi.childcare.shared.networking.requests.special.UpdateGruppiRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.*;
import com.polimi.childcare.shared.utils.EntitiesHelper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GitaSubsceneController extends NetworkedSubScene implements ISubSceneController
{
    public static final String FXML_PATH = "fxml/GitaScene.fxml";

    private Parent root;

    @FXML private AnchorPane rootPane;
    @FXML private Button btnRefresh;

    //Orfani
    @FXML private DragAndDropTableView<Bambino> tableOrfani;
    @FXML private TextField txtFilterOrfani;
    private OrderedFilteredList<Bambino> listOrfani;
    private FilterComponent<Bambino> filterOrfani;

    //Gite
    @FXML private DragAndDropTableView<Gita> tableGite;
    @FXML private TextField txtFilterGite;
    private OrderedFilteredList<Gita> listGite;
    private FilterComponent<Gita> filterGite;
    @FXML private Button btnAddGita;
    @FXML private Button btnEditPianoViaggi;
    @FXML private HBox hboxBottoniGita;

    //Mezzi
    @FXML private DragAndDropTableView<MezzoDiTrasporto> tableMezzi;
    @FXML private TextField txtFilterMezzi;
    private OrderedFilteredList<MezzoDiTrasporto> listMezzi;
    private FilterComponent<MezzoDiTrasporto> filterMezzi;
    @FXML private Button btnAddMezzo;

    //Addetti
    @FXML private DragAndDropTableView<Addetto> tableAddetti;

    //Gruppi
    @FXML private HBox hboxGruppi;
    @FXML private ImageView imgAddGroup;
    private List<Gruppo> receivedGroups;
    private List<GruppoContainerComponent> gruppoContainerComponents;
    @FXML private Button btnAutoAdd;
    @FXML private Button btnSaveGroups;

    @FXML
    protected void initialize()
    {
        //Generated
        gruppoContainerComponents = new ArrayList<>();

        //RefreshDelleListe
        btnRefresh.setOnMouseClicked(event -> RefreshData());

        //Istanzia oggetti
        listOrfani = new OrderedFilteredList<>();
        filterOrfani = new FilterComponent<>(listOrfani.predicateProperty());

        listGite = new OrderedFilteredList<>();
        filterGite = new FilterComponent<>(listGite.predicateProperty());

        listMezzi = new OrderedFilteredList<>();
        filterMezzi = new FilterComponent<>(listMezzi.predicateProperty());

        //Setup Filtri
        filterOrfani.addFilterField(txtFilterOrfani.textProperty(), (persona -> Filters.filterPersona(persona, txtFilterOrfani.getText())));
        filterGite.addFilterField(txtFilterGite.textProperty(), (g -> Filters.filterGita(g, txtFilterGite.getText())));
        filterMezzi.addFilterField(txtFilterMezzi.textProperty(), (m -> Filters.filterMezzo(m, txtFilterMezzi.getText())));

        if(btnEditPianoViaggi != null && hboxBottoniGita != null)
            hboxBottoniGita.getChildren().remove(btnEditPianoViaggi);

        setupTabellaAddetti();
        setupTabellaGite();
        setupTabellaMezzi();
        setupTabellaOrfani();

        btnAddMezzo.setOnMouseClicked(click -> ShowMezzoDiTrasporto(new MezzoDiTrasporto()));
        btnAddGita.setOnMouseClicked(click -> ShowGita(new Gita()));

        btnEditPianoViaggi.setOnMouseClicked(click -> {
            if(gruppoContainerComponents.size() == 0)
            {
                StageUtils.ShowAlert(Alert.AlertType.ERROR, "Non sono attualmente presenti dei gruppi, crea dei gruppi prima di impostare il piano viaggi!");
                return;
            }

            //Show piano viaggi per gita
            ShowPianoViaggi(tableGite.getSelectionModel().getSelectedItem());
        });

        setupGruppi();
        refreshGruppi(new ArrayList<>());

        //Effects
        if(imgAddGroup != null)
        {
            imgAddGroup.setOnMouseEntered(event -> EffectsUtils.AddShadow(imgAddGroup, 10));
            imgAddGroup.setOnMouseExited(event -> EffectsUtils.RemoveShadow(imgAddGroup));
        }
    }

    @Override
    public void attached(ISceneController sceneController, Object... args)
    {
        //Se sono stato collegato ad uno stage, imposto un titolo opportuno
        if(sceneController instanceof BaseStageController)
            ((BaseStageController)sceneController).requestSetTitle("Gita");

        RefreshData();
    }

    private void RefreshData()
    {
        //Provo ad aggiornare i dati
        networkOperationVault.submitOperation(new NetworkOperation(
                new FilteredAddettoRequest(0, 0, false),
                this::OnAddettiResponseRecived,
                true));
        networkOperationVault.submitOperation(new NetworkOperation(
                new FilteredMezzoDiTrasportoRequest(0, 0, false),
                this::OnMezziResponseRecived,
                true));
        networkOperationVault.submitOperation(new NetworkOperation(
                new FilteredGitaRequest(0, 0, false),
                this::OnGiteResponseRecived,
                true));
        networkOperationVault.submitOperation(new NetworkOperation(
                new FilteredGruppoRequest(0, 0, true),
                this::OnGruppiResponseRecived,
                true));

        refreshOrfani();
    }

    private void refreshOrfani()
    {
        networkOperationVault.submitOperation(new NetworkOperation(
                new GetBambiniSenzaGruppoRequest(),
                this::OnOrfaniResponseRecived,
                true));
    }

    private void redrawGruppi()
    {
        if(hboxGruppi != null)
        {
            hboxGruppi.getChildren().clear();
            for (GruppoContainerComponent containerComponent : gruppoContainerComponents)
                hboxGruppi.getChildren().add(containerComponent);

            if(imgAddGroup != null)
                hboxGruppi.getChildren().add(imgAddGroup);
        }
    }

    private void refreshGruppi(List<Gruppo> gruppi)
    {
        btnSaveGroups.setVisible(true);
        receivedGroups = gruppi;
        gruppoContainerComponents.clear();
        for (Gruppo gruppo : gruppi)
            addGruppoComponentForGruppo(gruppo);
        redrawGruppi();
    }

    private void updateIDGruppi()
    {
        int count = 1;
        for(GruppoContainerComponent containerComponent : gruppoContainerComponents)
            containerComponent.updateID(count++);
    }

    //region Setup

    private void setupTabellaAddetti()
    {
        TableColumn<Addetto, Integer> cMatricola = new TableColumn<>("Matricola");
        TableColumn<Addetto, String> cNome = new TableColumn<>("Nome");
        TableColumn<Addetto, String> cCognome = new TableColumn<>("Cognome");
        TableColumn<Addetto, String> cTelefoni = new TableColumn<>("Telefoni");

        cMatricola.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getID()));
        cNome.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getNome()));
        cCognome.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getCognome()));
        cTelefoni.setCellValueFactory(p -> new ReadOnlyStringWrapper(EntitiesHelper.getTelefoniStringFromIterable(p.getValue().getTelefoni())));

        cMatricola.setPrefWidth(75);
        cMatricola.setMinWidth(75);
        cMatricola.setMaxWidth(75);

        if(tableAddetti != null)
            tableAddetti.getColumns().addAll(cMatricola, cNome, cCognome, cTelefoni);
    }

    private void setupTabellaOrfani()
    {
        TableColumn<Bambino, Integer> cMatricola = new TableColumn<>("Matricola");
        TableColumn<Bambino, String> cNome = new TableColumn<>("Nome");
        TableColumn<Bambino, String> cCognome = new TableColumn<>("Cognome");
        TableColumn<Bambino, String> cCodiceFiscale = new TableColumn<>("Codice Fiscale");

        cMatricola.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getID()));
        cNome.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getNome()));
        cCognome.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getCognome()));
        cCodiceFiscale.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getCodiceFiscale()));

        cMatricola.setPrefWidth(75);
        cMatricola.setMinWidth(75);
        cMatricola.setMaxWidth(75);


        if(tableOrfani != null)
        {
            tableOrfani.getColumns().addAll(cMatricola, cNome, cCognome, cCodiceFiscale);
            listOrfani.comparatorProperty().bind(tableOrfani.comparatorProperty());
            tableOrfani.setItems(listOrfani.list());
        }
    }

    private void setupTabellaGite()
    {
        TableColumn<Gita, Integer> cID = new TableColumn<>("ID");
        TableColumn<Gita, String> cLuogo = new TableColumn<>("Luogo");
        TableColumn<Gita, LocalDate> cInizio = new TableColumn<>("Inizio");
        TableColumn<Gita, LocalDate> cFine = new TableColumn<>("Fine");

        cID.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getID()));
        cLuogo.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getLuogo()));
        cInizio.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getDataInizio()));
        cFine.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getDataFine()));

        cID.setPrefWidth(75);
        cID.setMinWidth(75);
        cID.setMaxWidth(75);

        if(tableGite != null)
        {
            tableGite.getColumns().addAll(cID, cLuogo, cInizio, cFine);
            listGite.comparatorProperty().bind(tableGite.comparatorProperty());
            tableGite.setItems(listGite.list());

            tableGite.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                updateGitaButtons(newValue);
            });

            tableGite.setRowFactory(tv -> new TableRow<Gita>() {
                @Override
                public void updateItem(Gita item, boolean empty) {
                    super.updateItem(item, empty) ;
                    if (item == null) {
                        setStyle("");
                    } else if (item.getDataInizio().atStartOfDay().isBefore(LocalDateTime.now()) &&
                            item.getDataFine().plusDays(1).atStartOfDay().isAfter(LocalDateTime.now())) {
                        //Gita attiva
                        setStyle("-fx-background-color: DarkSeaGreen;");
                    } else {
                        setStyle("");
                    }

                    tableGite.setOnMousePressed((event -> {
                        if(event.isPrimaryButtonDown() && event.getClickCount() == 2 && tableGite.getSelectionModel().getSelectedItem() != null)
                            ShowGita(tableGite.getSelectionModel().getSelectedItem());
                    }));
                }
            });
        }
    }

    private void updateGitaButtons(Gita selectedGita)
    {
        if(btnEditPianoViaggi != null && hboxBottoniGita != null)
        {
            if (selectedGita == null || gruppoContainerComponents.size() == 0)
                hboxBottoniGita.getChildren().remove(btnEditPianoViaggi);
            else if(!hboxBottoniGita.getChildren().contains(btnEditPianoViaggi))
                hboxBottoniGita.getChildren().add(btnEditPianoViaggi);
        }
    }

    private void setupTabellaMezzi()
    {
        TableColumn<MezzoDiTrasporto, Integer> cID = new TableColumn<>("ID");
        TableColumn<MezzoDiTrasporto, String> cTarga = new TableColumn<>("Targa");
        TableColumn<MezzoDiTrasporto, String> cFornitore = new TableColumn<>("Fornitore");
        TableColumn<MezzoDiTrasporto, Integer> cCapienza = new TableColumn<>("Capienza");
        TableColumn<MezzoDiTrasporto, Integer> cCosto = new TableColumn<>("Costo/h");

        cID.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getID()));
        cTarga.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getTarga()));
        cFornitore.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getFornitore() != null ? p.getValue().getFornitore().getRagioneSociale() : ""));
        cCapienza.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getCapienza()));
        cCosto.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getCostoOrario()));

        cID.setPrefWidth(75);
        cID.setMinWidth(75);
        cID.setMaxWidth(75);

        if(tableMezzi != null)
        {
            tableMezzi.getColumns().addAll(cID, cTarga, cFornitore, cCapienza, cCosto);
            listMezzi.comparatorProperty().bind(tableMezzi.comparatorProperty());
            tableMezzi.setItems(listMezzi.list());

            tableMezzi.setOnMousePressed((event -> {
                if(event.isPrimaryButtonDown() && event.getClickCount() == 2 && tableMezzi.getSelectionModel().getSelectedItem() != null)
                    ShowMezzoDiTrasporto(tableMezzi.getSelectionModel().getSelectedItem());
            }));
        }
    }

    private void setupGruppi()
    {
        imgAddGroup.setOnMouseClicked((ignored) -> {
            //Aggiunge un nuovo gruppo infondo alla lista dei gruppi
            Gruppo gruppo = new Gruppo();
            gruppo.unsafeSetID(gruppoContainerComponents.size() + 1);
            addGruppoComponentForGruppo(gruppo);
            redrawGruppi();
        });

        btnAutoAdd.setOnMouseClicked((event -> {
            if(gruppoContainerComponents.size() > 0 && listOrfani.total() > 0) {
                int orphansPerGroup = (int) Math.floor(listOrfani.total() / gruppoContainerComponents.size());

                int remainingGroups = gruppoContainerComponents.size();
                for (GruppoContainerComponent containerComponent : gruppoContainerComponents) {
                    for (int i = 0; i < orphansPerGroup; i++) {
                        Bambino bambino = listOrfani.get(i * (remainingGroups - 1));
                        containerComponent.addBambino(bambino);
                        listOrfani.remove(bambino);
                    }
                    remainingGroups--;
                }

                //Qualche residuo
                if (listOrfani.total() > 0) {
                    for (int i = 0; i < listOrfani.total(); i++) {
                        Bambino bambino = listOrfani.get(i);
                        gruppoContainerComponents.get(i % gruppoContainerComponents.size()).addBambino(bambino);
                        listOrfani.remove(bambino);
                    }
                }
            }
        }));

        btnSaveGroups.setOnMouseClicked(click ->
        {
            List<Gruppo> newGruppi = new ArrayList<>();
            for (GruppoContainerComponent gruppoContainerComponent : gruppoContainerComponents)
                newGruppi.add(gruppoContainerComponent.getCurrentGruppoRappresentation());

            boolean modified = newGruppi.size() != receivedGroups.size();

            //Se ci sono delle gite attive, notifico dell'effetto delle modifiche
            if(listGite.total() > 0 && modified)
            {
                Optional<ButtonType> response = StageUtils.ShowAlertWithButtons(Alert.AlertType.INFORMATION,
                        "Attenzione: la modifica dei gruppi con delle gite presenti renderà necessaria la modifica dei piani viaggo legati a quest'ultime.",
                        ButtonType.CANCEL, ButtonType.OK);
                if(!response.isPresent() || response.get() == ButtonType.CANCEL)
                    return;
            }


            if(!modified)
            {
                //Controllo eventuali aggiornamenti
                for(Gruppo gruppo : newGruppi)
                {
                    int index = receivedGroups.indexOf(gruppo);
                    if(index < 0)
                    {
                        modified = true;
                        break;
                    }

                    Gruppo previousGruppo = receivedGroups.get(index);
                    modified = previousGruppo.hashCode() != gruppo.hashCode() ||
                                !previousGruppo.equals(gruppo) ||
                                previousGruppo.getBambini().size() != gruppo.getBambini().size() ||
                                previousGruppo.getSorvergliante() != gruppo.getSorvergliante() ||
                            !CollectionUtils.isEqualCollection(previousGruppo.getBambini(), gruppo.getBambini());
                    if(modified)
                        break;
                }
            }

            if(modified)
            {
                networkOperationVault.submitOperation(new NetworkOperation(new UpdateGruppiRequest(newGruppi), response ->
                {
                    if(StageUtils.HandleResponseError(response, "Errore nell'impostazione dei gruppi", p -> p.getCode() == 200))
                        return;
                    RefreshData();
                }, true));
            }
        });
    }

    private void addGruppoComponentForGruppo(Gruppo gruppo)
    {
        GruppoContainerComponent containerComponent = new GruppoContainerComponent(gruppo);
        containerComponent.setDragEnabled(true);
        hboxGruppi.getChildren().add(containerComponent);
        gruppoContainerComponents.add(containerComponent);

        containerComponent.setOnBambinoAggiunto(((element, source, target) ->
        {
            listOrfani.remove(element);
            for(GruppoContainerComponent component : gruppoContainerComponents)
                if(component != containerComponent)
                    component.removeBambino(element);
        }));

        containerComponent.setOnBambinoRimosso(((element, source, target) -> {
            listOrfani.add(element);
        }));

        containerComponent.setOnDeleteClicked(ignored -> {
            for(Bambino bambino : containerComponent.getCurrentGruppoRappresentation().getBambini())
                listOrfani.add(bambino);
            gruppoContainerComponents.remove(containerComponent);
            hboxGruppi.getChildren().remove(containerComponent);
            updateIDGruppi();
        });
    }

    //endregion

    //region Callback

    private void OnAddettiResponseRecived(BaseResponse response)
    {
        networkOperationVault.operationDone(FilteredAddettoRequest.class);
        if(StageUtils.HandleResponseError(response, "Errore durante la connessione al server", r -> r instanceof ListAddettiResponse))
            return;

        ListAddettiResponse listAddettiResponse = (ListAddettiResponse)response;
        tableAddetti.setItems(FXCollections.observableArrayList(listAddettiResponse.getPayload()));
    }

    private void OnGiteResponseRecived(BaseResponse response)
    {
        networkOperationVault.operationDone(FilteredGitaRequest.class);
        if(StageUtils.HandleResponseError(response, "Errore durante la connessione al server", r -> r instanceof ListGitaResponse))
            return;

        ListGitaResponse listGiteResponse = (ListGitaResponse)response;
        listGite.updateDataSet(FXCollections.observableArrayList(listGiteResponse.getPayload()));
    }

    private void OnMezziResponseRecived(BaseResponse response)
    {
        networkOperationVault.operationDone(FilteredMezzoDiTrasportoRequest.class);
        if(StageUtils.HandleResponseError(response, "Errore durante la connessione al server", r -> r instanceof ListMezzoDiTrasportoResponse))
            return;

        ListMezzoDiTrasportoResponse listMezziResponse = (ListMezzoDiTrasportoResponse)response;
        listMezzi.updateDataSet(FXCollections.observableArrayList(listMezziResponse.getPayload()));
    }

    private void OnOrfaniResponseRecived(BaseResponse response)
    {
        networkOperationVault.operationDone(GetBambiniSenzaGruppoRequest.class);
        if(StageUtils.HandleResponseError(response, "Errore durante la connessione al server", r -> r instanceof ListBambiniResponse))
            return;

        ListBambiniResponse listOrfaniResponse = (ListBambiniResponse)response;
        listOrfani.updateDataSet(FXCollections.observableArrayList(listOrfaniResponse.getPayload()));
    }

    private void OnGruppiResponseRecived(BaseResponse response)
    {
        networkOperationVault.operationDone(FilteredGruppoRequest.class);
        if(StageUtils.HandleResponseError(response, "Errore durante la connessione al server", r -> r instanceof ListGruppoResponse))
            return;

        ListGruppoResponse listOrfaniResponse = (ListGruppoResponse)response;

        refreshGruppi(listOrfaniResponse.getPayload());
    }

    //endregion

    //region Stages

    private void ShowMezzoDiTrasporto(MezzoDiTrasporto mezzoDiTrasporto)
    {
        try {
            ChildcareBaseStageController showEditMezzo = new ChildcareBaseStageController();
            showEditMezzo.setContentScene(getClass().getClassLoader().getResource(EditMezzoStageController.FXML_PATH), mezzoDiTrasporto);
            showEditMezzo.initOwner(getRoot().getScene().getWindow());
            showEditMezzo.setOnClosingCallback((returnArgs) -> RefreshData());
            showEditMezzo.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ShowGita(Gita gita)
    {
        try {
            ChildcareBaseStageController showEditMezzo = new ChildcareBaseStageController();
            showEditMezzo.setContentScene(getClass().getClassLoader().getResource(EditGitaStageController.FXML_PATH), gita);
            showEditMezzo.initOwner(getRoot().getScene().getWindow());
            showEditMezzo.setOnClosingCallback((returnArgs) -> RefreshData());
            showEditMezzo.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ShowPianoViaggi(Gita gita)
    {
        try {
            ChildcareBaseStageController showPianoViaggi = new ChildcareBaseStageController();
            showPianoViaggi.setContentScene(getClass().getClassLoader().getResource(EditPianoViaggiController.FXML_PATH), gita);
            showPianoViaggi.initOwner(getRoot().getScene().getWindow());
            showPianoViaggi.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //endregion

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
