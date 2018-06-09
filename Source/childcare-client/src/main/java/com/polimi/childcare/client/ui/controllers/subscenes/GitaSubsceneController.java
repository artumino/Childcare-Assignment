package com.polimi.childcare.client.ui.controllers.subscenes;

import com.polimi.childcare.client.shared.networking.NetworkOperation;
import com.polimi.childcare.client.ui.OrderedFilteredList;
import com.polimi.childcare.client.ui.components.FilterComponent;
import com.polimi.childcare.client.ui.controllers.BaseStageController;
import com.polimi.childcare.client.ui.controllers.ISceneController;
import com.polimi.childcare.client.ui.controllers.ISubSceneController;
import com.polimi.childcare.client.ui.controls.DragAndDropTableView;
import com.polimi.childcare.client.ui.controls.GruppoContainerComponent;
import com.polimi.childcare.client.ui.filters.Filters;
import com.polimi.childcare.client.ui.utils.EffectsUtils;
import com.polimi.childcare.client.ui.utils.StageUtils;
import com.polimi.childcare.shared.entities.*;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredAddettoRequest;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredGitaRequest;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredMezzoDiTrasportoRequest;
import com.polimi.childcare.shared.networking.requests.special.GetBambiniSenzaGruppoRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListAddettiResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListBambiniResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListGitaResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListMezzoDiTrasportoResponse;
import com.polimi.childcare.shared.utils.EntitiesHelper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

import java.util.ArrayList;
import java.util.List;

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

    //Mezzi
    @FXML private DragAndDropTableView<MezzoDiTrasporto> tableMezzi;
    @FXML private TextField txtFilterMezzi;
    private OrderedFilteredList<MezzoDiTrasporto> listMezzi;
    private FilterComponent<MezzoDiTrasporto> filterMezzi;
    @FXML private Button btnAddMezz0;

    //Addetti
    @FXML private DragAndDropTableView<Addetto> tableAddetti;

    //Gruppi
    @FXML private HBox hboxGruppi;
    @FXML private ImageView imgAddGroup;

    @FXML
    protected void initialize()
    {
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

        setupTabellaAddetti();
        setupTabellaGite();
        setupTabellaMezzi();
        setupTabellaOrfani();
        redrawGruppi();

        //Effects
        if(imgAddGroup != null)
        {
            imgAddGroup.setOnMouseEntered(event -> EffectsUtils.AddShadow(imgAddGroup, 2));
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
        List<Gruppo> gruppoList = new ArrayList<>(5);
        for(int i = 0; i < 5; i++)
        {
            Gruppo gruppo = new Gruppo();
            gruppo.unsafeSetID(i);
            gruppoList.add(gruppo);
        }

        if(hboxGruppi != null)
        {
            hboxGruppi.getChildren().clear();
            for (Gruppo gruppo : gruppoList) {
                GruppoContainerComponent containerComponent = new GruppoContainerComponent(gruppo);
                containerComponent.setOnDeleteClicked(ignored -> {});
                containerComponent.setDragEnabled(true);
                hboxGruppi.getChildren().add(containerComponent);
            }

            if(imgAddGroup != null)
                hboxGruppi.getChildren().add(imgAddGroup);
        }
    }

    //region Setup

    private void setupTabellaAddetti()
    {
        TableColumn<Addetto, String> cMatricola = new TableColumn<>("Matricola");
        TableColumn<Addetto, String> cNome = new TableColumn<>("Nome");
        TableColumn<Addetto, String> cCognome = new TableColumn<>("Cognome");
        TableColumn<Addetto, String> cTelefoni = new TableColumn<>("Telefoni");

        cMatricola.setCellValueFactory(p -> new ReadOnlyStringWrapper(String.valueOf(p.getValue().getID())));
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
        TableColumn<Bambino, String> cMatricola = new TableColumn<>("Matricola");
        TableColumn<Bambino, String> cNome = new TableColumn<>("Nome");
        TableColumn<Bambino, String> cCognome = new TableColumn<>("Cognome");
        TableColumn<Bambino, String> cCodiceFiscale = new TableColumn<>("Codice Fiscale");

        cMatricola.setCellValueFactory(p -> new ReadOnlyStringWrapper(String.valueOf(p.getValue().getID())));
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
        TableColumn<Gita, String> cID = new TableColumn<>("ID");
        TableColumn<Gita, String> cLuogo = new TableColumn<>("Luogo");
        TableColumn<Gita, String> cInizio = new TableColumn<>("Inizio");
        TableColumn<Gita, String> cFine = new TableColumn<>("Fine");

        cID.setCellValueFactory(p -> new ReadOnlyStringWrapper(String.valueOf(p.getValue().getID())));
        cLuogo.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getLuogo()));
        cInizio.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getDataInizio().toString()));
        cFine.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getDataFine().toString()));

        cID.setPrefWidth(75);
        cID.setMinWidth(75);
        cID.setMaxWidth(75);

        if(tableGite != null)
        {
            tableGite.getColumns().addAll(cID, cLuogo, cInizio, cFine);
            listGite.comparatorProperty().bind(tableGite.comparatorProperty());
            tableGite.setItems(listGite.list());
        }
    }

    private void setupTabellaMezzi()
    {
        TableColumn<MezzoDiTrasporto, String> cID = new TableColumn<>("ID");
        TableColumn<MezzoDiTrasporto, String> cTarga = new TableColumn<>("Targa");
        TableColumn<MezzoDiTrasporto, String> cFornitore = new TableColumn<>("Fornitore");
        TableColumn<MezzoDiTrasporto, String> cCapienza = new TableColumn<>("Capienza");
        TableColumn<MezzoDiTrasporto, String> cCosto = new TableColumn<>("Costo/h");

        cID.setCellValueFactory(p -> new ReadOnlyStringWrapper(String.valueOf(p.getValue().getID())));
        cTarga.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getTarga()));
        cFornitore.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getFornitore() != null ? p.getValue().getFornitore().getRagioneSociale() : ""));
        cCapienza.setCellValueFactory(p -> new ReadOnlyStringWrapper(String.valueOf(p.getValue().getCapienza())));
        cCosto.setCellValueFactory(p -> new ReadOnlyStringWrapper(String.valueOf(p.getValue().getCostoOrario())));

        cID.setPrefWidth(75);
        cID.setMinWidth(75);
        cID.setMaxWidth(75);

        if(tableMezzi != null)
        {
            tableMezzi.getColumns().addAll(cID, cTarga, cFornitore, cCapienza, cCosto);
            listMezzi.comparatorProperty().bind(tableMezzi.comparatorProperty());
            tableMezzi.setItems(listMezzi.list());
        }
    }

    //endregion

    //region Callback

    private void OnAddettiResponseRecived(BaseResponse response)
    {
        if(StageUtils.HandleResponseError(response, "Errore durante la connessione al server", r -> r instanceof ListAddettiResponse))
            return;

        ListAddettiResponse listAddettiResponse = (ListAddettiResponse)response;
        tableAddetti.setItems(FXCollections.observableArrayList(listAddettiResponse.getPayload()));
    }

    private void OnGiteResponseRecived(BaseResponse response)
    {
        if(StageUtils.HandleResponseError(response, "Errore durante la connessione al server", r -> r instanceof ListGitaResponse))
            return;

        ListGitaResponse listGiteResponse = (ListGitaResponse)response;
        listGite.updateDataSet(FXCollections.observableArrayList(listGiteResponse.getPayload()));
    }

    private void OnMezziResponseRecived(BaseResponse response)
    {
        if(StageUtils.HandleResponseError(response, "Errore durante la connessione al server", r -> r instanceof ListMezzoDiTrasportoResponse))
            return;

        ListMezzoDiTrasportoResponse listMezziResponse = (ListMezzoDiTrasportoResponse)response;
        listMezzi.updateDataSet(FXCollections.observableArrayList(listMezziResponse.getPayload()));
    }

    private void OnOrfaniResponseRecived(BaseResponse response)
    {
        if(StageUtils.HandleResponseError(response, "Errore durante la connessione al server", r -> r instanceof ListBambiniResponse))
            return;

        ListBambiniResponse listOrfaniResponse = (ListBambiniResponse)response;
        listOrfani.updateDataSet(FXCollections.observableArrayList(listOrfaniResponse.getPayload()));
    }

    //endregion

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
