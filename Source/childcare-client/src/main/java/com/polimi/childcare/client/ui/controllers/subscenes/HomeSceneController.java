package com.polimi.childcare.client.ui.controllers.subscenes;

import com.jfoenix.controls.JFXButton;
import com.polimi.childcare.client.shared.networking.ClientNetworkManager;
import com.polimi.childcare.client.shared.networking.NetworkOperation;
import com.polimi.childcare.client.ui.OrderedFilteredList;
import com.polimi.childcare.client.ui.components.FilterComponent;
import com.polimi.childcare.client.ui.controllers.BaseStageController;
import com.polimi.childcare.client.ui.controllers.ChildcareBaseStageController;
import com.polimi.childcare.client.ui.controllers.ISceneController;
import com.polimi.childcare.client.ui.controllers.ISubSceneController;
import com.polimi.childcare.client.ui.filters.Filters;
import com.polimi.childcare.client.ui.utils.StageUtils;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.entities.Pasto;
import com.polimi.childcare.shared.entities.RegistroPresenze;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredBambiniRequest;
import com.polimi.childcare.shared.networking.requests.special.FilteredLastPresenzaRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListBambiniResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListRegistroPresenzeResponse;
import com.polimi.childcare.shared.utils.EntitiesHelper;
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
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

public class HomeSceneController implements ISubSceneController
{
    private Parent root;

    @FXML private AnchorPane rootPane;
    @FXML private Button btnRefresh;

    //Presenze
    @FXML private TableView<Bambino> tablePresenze;
    @FXML private TextField txtFiltro;

    //Menu del Giorno
    @FXML private Text txtNomeMenuDelGiorno;
    @FXML private VBox vboxMenuDelGiorno;
    @FXML private TableView<Pasto> tableMenuDelGiorno;

    //Gite
    @FXML private TableView<Pasto> tableGite;

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

        //RefreshDelleListe
        btnRefresh.setOnMouseClicked(event -> RefreshData());

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
        editPresenza.setCellFactory((cellData) -> new TableCell<Bambino, RegistroPresenze.StatoPresenza>()
        {
            @Override
            protected void updateItem(RegistroPresenze.StatoPresenza item, boolean empty)
            {
                if(!empty)
                {
                    Bambino bambino = (Bambino) getTableRow().getItem();
                    RegistroPresenze statoPresenza = statoPresenze.get(bambino);
                    RegistroPresenze.StatoPresenza statoCorrente = ((statoPresenza != null) ?
                            (statoPresenza.getTimeStamp().toInstant(ZoneOffset.UTC).toEpochMilli() >= LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()) ?
                                    statoPresenza.getStato() :
                                    RegistroPresenze.StatoPresenza.Assente
                            : RegistroPresenze.StatoPresenza.Assente);

                    Button graphicButton = new JFXButton(statoCorrente.name());
                    graphicButton.setOnMouseClicked((event -> OnEditPresenzaBambino(bambino)));

                    graphicButton.setStyle("-fx-background-color: #f00;");
                    switch (statoCorrente) {
                        case Presente:
                            graphicButton.setStyle("-fx-background-color: #0f0;");
                            break;
                        case EntratoInRitardo:
                            graphicButton.setStyle("-fx-background-color: #ff0;");
                            break;
                        case UscitoInAnticipo:
                            graphicButton.setStyle("-fx-background-color: #00f;");
                            break;
                    }
                    graphicButton.setStyle(graphicButton.getStyle() + "-fx-text-fill: #fff;");
                    setGraphic(graphicButton);
                }
                else
                    setGraphic(null);
            }
        });
        editPresenza.setMinWidth(100);
        editPresenza.setPrefWidth(100);
        editPresenza.setMaxWidth(100);

        id.setCellValueFactory((cellData) -> new ReadOnlyObjectWrapper<>(cellData.getValue().getID()));

        filterBambini.addFilterField(txtFiltro.textProperty(), (persona -> Filters.filterPersona(persona, txtFiltro.getText())));

        if(tablePresenze != null) {
            tablePresenze.getColumns().addAll(id, name, surname, fiscalCode, editPresenza);
            listaBambini.comparatorProperty().bind(tablePresenze.comparatorProperty());
            tablePresenze.setItems(listaBambini.list());
            //tableList.setColumnResizePolicy(p -> true);
        }
    }

    @Override
    public void attached(ISceneController sceneController, Object... args)
    {
        //Se sono stato collegato ad uno stage, imposto un titolo opportuno
        if(sceneController instanceof BaseStageController)
            ((BaseStageController)sceneController).requestSetTitle("Home");

        RefreshData();
    }

    private void RefreshData()
    {
        //Provo ad aggiornare i dati
        ClientNetworkManager.getInstance().submitOperation(new NetworkOperation(
                new FilteredBambiniRequest(0, 0, false, null, null),
                this::OnBambiniResponseRecived,
                true));

        ClientNetworkManager.getInstance().submitOperation(new NetworkOperation(
                new FilteredLastPresenzaRequest(0, 0, false, null, null),
                this::OnPresenzeRecived,
                true));
    }

    private void OnEditPresenzaBambino(Bambino bambino)
    {
        try {
            ChildcareBaseStageController setPresenzeStage = new ChildcareBaseStageController();
            setPresenzeStage.setContentScene(getClass().getClassLoader().getResource("fxml/stages/presenze/SetPresenzaStage.fxml"), bambino);
            setPresenzeStage.initModality(Modality.WINDOW_MODAL);
            setPresenzeStage.initOwner(getRoot().getScene().getWindow());
            setPresenzeStage.setOnClosingCallback((returnArgs) -> {
                if(returnArgs.length > 0 && returnArgs[0] instanceof RegistroPresenze)
                {
                    RegistroPresenze nuovoStatoPresenza = (RegistroPresenze)returnArgs[0];
                    statoPresenze.put(nuovoStatoPresenza.getBambino(), nuovoStatoPresenza);
                }

                tablePresenze.refresh();
            });
            setPresenzeStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void OnBambiniResponseRecived(BaseResponse response)
    {
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
