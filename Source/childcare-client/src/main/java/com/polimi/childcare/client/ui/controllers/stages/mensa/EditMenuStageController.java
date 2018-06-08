package com.polimi.childcare.client.ui.controllers.stages.mensa;

import com.polimi.childcare.client.shared.networking.NetworkOperation;
import com.polimi.childcare.client.ui.OrderedFilteredList;
import com.polimi.childcare.client.ui.components.FilterComponent;
import com.polimi.childcare.client.ui.filters.Filters;
import com.polimi.childcare.shared.entities.*;
import com.polimi.childcare.shared.entities.Menu;
import com.polimi.childcare.shared.entities.tuples.MenuAvviso;
import com.polimi.childcare.client.ui.constants.ToolbarButtons;
import com.polimi.childcare.client.ui.controllers.BaseStageController;
import com.polimi.childcare.client.ui.controllers.ChildcareBaseStageController;
import com.polimi.childcare.client.ui.controllers.ISceneController;
import com.polimi.childcare.client.ui.controllers.ISubSceneController;
import com.polimi.childcare.client.ui.controllers.stages.networking.BlockingNetworkOperationStageController;
import com.polimi.childcare.client.ui.controllers.subscenes.NetworkedSubScene;
import com.polimi.childcare.client.ui.controls.DragAndDropTableView;
import com.polimi.childcare.client.ui.controls.LabelRecurrenceComponent;
import com.polimi.childcare.client.ui.controls.LabelTextViewComponent;
import com.polimi.childcare.client.ui.utils.StageUtils;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredMenuRequest;
import com.polimi.childcare.shared.networking.requests.setters.SetMenuRequest;
import com.polimi.childcare.shared.networking.requests.special.GetPersoneWithDisagnosiRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListMenuResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListPersoneResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListResponse;
import com.polimi.childcare.shared.utils.EntitiesHelper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class EditMenuStageController extends NetworkedSubScene implements ISubSceneController
{
    public static final String FXML_PATH = "fxml/stages/mensa/EditMenuStage.fxml";

    private Menu linkedMenu;
    private List<Persona> personeDiagnosi;
    private ChildcareBaseStageController stageController;
    private Parent root;

    @FXML private AnchorPane rootPane;
    @FXML private AnchorPane loadingLayout;

    @FXML private TabPane layoutTabPane;

    @FXML private LabelTextViewComponent txtNome;
    @FXML private LabelRecurrenceComponent rcRecurrence;
    @FXML private DragAndDropTableView<Pasto> tablePasti;

    private OrderedFilteredList<MenuAvviso> listMenuAvvisi;
    private FilterComponent<MenuAvviso> filterMenuAvvisi;
    @FXML private TextField txtFilterAvvisi;
    @FXML private ImageView imgRemoveFilter;
    @FXML private DragAndDropTableView<MenuAvviso> tableAvvisi;

    @FXML private Button btnSalva;
    @FXML private Button btnElimina;

    @Override
    public void attached(ISceneController sceneController, Object... args)
    {
        if(sceneController instanceof ChildcareBaseStageController)
            this.stageController = (ChildcareBaseStageController)sceneController;


        if(this.stageController != null)
        {
            this.stageController.setToolbarButtonsVisibilityMask((byte)(ToolbarButtons.Close | ToolbarButtons.Minimize));

            if(args != null && args.length > 0 && args[0] instanceof Menu)
                this.linkedMenu = (Menu)args[0];


            if(linkedMenu != null && linkedMenu.getID() != 0)
            {
                this.loadingLayout.setVisible(true);
                networkOperationVault.submitOperation(new NetworkOperation(new FilteredMenuRequest(this.linkedMenu.getID(), true),
                        response ->
                        {
                            networkOperationVault.operationDone(FilteredMenuRequest.class);

                            if(StageUtils.HandleResponseError(response, "Errore, risposta non corretta dal server",
                                    p -> p instanceof ListMenuResponse && ((ListMenuResponse)p).getPayload().size() != 0))
                                stageController.requestClose();
                            else
                            {
                                this.linkedMenu = ((ListMenuResponse)response).getPayload().get(0);
                                updateData();
                                this.loadingLayout.setVisible(false);
                            }
                        },
                        true));
            }

            //Aggiorno avvisi in base alle persone
            networkOperationVault.submitOperation(new NetworkOperation(new GetPersoneWithDisagnosiRequest(0,0,false),
                    response ->
                    {
                        networkOperationVault.operationDone(FilteredMenuRequest.class);

                        if(StageUtils.HandleResponseError(response, "Errore, risposta non corretta dal server",
                                p -> p instanceof ListPersoneResponse && ((ListPersoneResponse)p).getPayload().size() != 0))
                            stageController.requestClose();
                        else
                        {
                            this.personeDiagnosi = ((ListPersoneResponse)response).getPayload();
                            updateData();
                            this.loadingLayout.setVisible(false);
                        }
                    },
                    true));

            this.listMenuAvvisi = new OrderedFilteredList<>();
            this.filterMenuAvvisi = new FilterComponent<>(this.listMenuAvvisi.predicateProperty());

            //Tabelle Generali
            printMenuDetails();
            setupPasti();
            setupAvvisi();

            //Bottoni
            if(linkedMenu.getID() == 0) //Nascondo per persone nuove
                btnElimina.setVisible(false);

            //Filtro
            if(txtFilterAvvisi != null)
            {
                txtFilterAvvisi.textProperty().addListener((observable, oldValue, newValue) -> {
                    imgRemoveFilter.setVisible(!newValue.isEmpty());
                });
                this.filterMenuAvvisi.addFilterField(txtFilterAvvisi.textProperty(), p ->
                {
                    String queryString = txtFilterAvvisi.getText().toLowerCase().trim();
                    return p != null && ((p.getLinkedPersona() != null && Filters.filterPersona(p.getLinkedPersona(), queryString)) || p.getMessage().contains(queryString));
                });
            }

            imgRemoveFilter.setOnMouseClicked((click) -> txtFilterAvvisi.textProperty().set(""));

            btnElimina.setOnMouseClicked(click -> {
                    Optional<ButtonType> result = StageUtils.ShowAlertWithButtons(Alert.AlertType.CONFIRMATION,
                            "Sei sicuro di voler cancellare " + linkedMenu.getNome() + "?",
                            ButtonType.YES, ButtonType.NO);

                    if(result.isPresent() && result.get().equals(ButtonType.YES))
                    {
                        //Confermata la cancellazione
                        ShowBlockingNetworkOperationStage(new NetworkOperation(new SetMenuRequest(linkedMenu, true, linkedMenu.consistecyHashCode()),null, true), (resultArgs) -> {
                           if(resultArgs != null && resultArgs.length > 0 && resultArgs[0] instanceof BaseResponse)
                           {
                               BaseResponse response = (BaseResponse)resultArgs[0];

                               if(!(response instanceof BadRequestResponse))
                               {
                                   stageController.close();
                                   return;
                               }

                               StageUtils.HandleResponseError(response, "Errore nella cancellazione del menu", p -> true);
                           }
                        });
                    }
            });

            btnSalva.setOnMouseClicked(this::SaveClicked);

            //Aggiorna l'interfaccia con i presenti in linkedPersona
            updateData();
        }
    }

    private void updateData()
    {
        //Pasti
        if(linkedMenu != null && tablePasti != null && linkedMenu.getPasti() != null)
        {
            tablePasti.getItems().clear();
            tablePasti.getItems().addAll(linkedMenu.getPasti());
        }

        updateAvvisi();
        printMenuDetails();
    }

    private void updateAvvisi()
    {
        Menu dummyMenu = getCurrentMenu();
        //Avvisi
        if(dummyMenu != null && tableAvvisi != null && personeDiagnosi != null)
            listMenuAvvisi.updateDataSet(dummyMenu.getAvvisi(personeDiagnosi));
    }

    private void setupPasti()
    {
        TableColumn<Pasto, String> cNome = new TableColumn<>("Nome");
        TableColumn<Pasto, String> cFornitore = new TableColumn<>("Fornitore");
        cNome.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getNome()));
        cFornitore.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getFornitore() != null ? c.getValue().getFornitore().getRagioneSociale() : ""));


        if(tablePasti != null)
        {
            tablePasti.getItems().addListener((ListChangeListener<Pasto>) c -> {
                updateAvvisi();
            });
            tablePasti.getColumns().addAll(cNome, cFornitore);
            tablePasti.dragForClass(Pasto.class);
        }
    }

    private void setupAvvisi()
    {
        TableColumn<MenuAvviso, String> cAvviso = new TableColumn<>("Avviso");

        cAvviso.setCellFactory((cellData) -> new TableCell<MenuAvviso, String>() {
            @Override
            protected void updateItem(String item, boolean empty)
            {
                if(!empty && getTableRow().getItem() != null)
                {
                    MenuAvviso avviso = (MenuAvviso) getTableRow().getItem();
                    Text text = new Text();
                    setMaxWidth(Double.MAX_VALUE);
                    setPrefHeight(Control.USE_COMPUTED_SIZE);
                    text.wrappingWidthProperty().bind(cAvviso.widthProperty());

                    //Imposta il colore
                    if(avviso.getSeverity() != null)
                    {
                        switch (avviso.getSeverity()) {
                            case Warning:
                                text.setFill(Color.ORANGE);
                                break;
                            case Critical:
                                text.setFill(Color.RED);
                                break;
                        }
                    }

                    text.textProperty().set(avviso.getMessage());
                    setGraphic(text);
                }
                else
                    setGraphic(null);
            }
        });


        if(tableAvvisi != null)
        {
            tableAvvisi.getColumns().addAll(cAvviso);
            listMenuAvvisi.comparatorProperty().bind(tableAvvisi.comparatorProperty());
            tableAvvisi.setItems(listMenuAvvisi.list());
        }
    }

    private void printMenuDetails()
    {

        if(this.linkedMenu != null)
        {
            //Setup title
            if(this.linkedMenu.getNome() != null)
                this.stageController.requestSetTitle(linkedMenu.getNome());
            else
                this.stageController.requestSetTitle("Crea " + linkedMenu.getClass().getSimpleName());

            //Setup fields
            txtNome.setTextFieldText(linkedMenu.getNome());
            rcRecurrence.setRecurrence(linkedMenu.getRicorrenza());
        }
    }

    private void ShowBlockingNetworkOperationStage(NetworkOperation networkOperation, BaseStageController.OnStageClosingCallback callback)
    {
        try {
            ChildcareBaseStageController blockingOperationController = new ChildcareBaseStageController();
            blockingOperationController.setContentScene(getClass().getClassLoader().getResource(BlockingNetworkOperationStageController.FXML_PATH), networkOperation);
            blockingOperationController.initOwner(getRoot().getScene().getWindow());
            blockingOperationController.initModality(Modality.APPLICATION_MODAL); //Blocco tutto
            blockingOperationController.setOnClosingCallback(callback);
            blockingOperationController.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Menu getCurrentMenu()
    {
        Menu newMenu = new Menu();

        //Imposta dettagli
        newMenu.unsafeSetID(linkedMenu.getID());
        newMenu.setNome(txtNome.getTextFieldText());
        newMenu.setRicorrenza(rcRecurrence.getRecurrence());

        //Imposta relazioni persona
        for(Pasto pasto : tablePasti.getItems())
            newMenu.addPasto(pasto);

        newMenu.setAttivo(newMenu.getRicorrenza() != 0);
        return newMenu;
    }

    private void SaveClicked(MouseEvent ignored)
    {
        Menu newMenu = getCurrentMenu();

        //Manda richiesta
        ShowBlockingNetworkOperationStage(new NetworkOperation(new SetMenuRequest(newMenu, false, linkedMenu.consistecyHashCode()), null, true), returnArgs ->
        {
            if(returnArgs != null && returnArgs.length > 0 && returnArgs[0] instanceof BaseResponse)
            {
                BaseResponse response = (BaseResponse)returnArgs[0];
                String errorMessage = "Impossibile eseguire l'operazione di modifica/inserimento, si è verificato un errore sconosciuto.";

                if(StageUtils.HandleResponseError(response, errorMessage, p -> p.getCode() == 200 || p instanceof ListResponse))
                    return;

                if(response instanceof ListResponse)
                {
                    if(((ListResponse) response).getPayload() == null || ((ListResponse) response).getPayload().size() == 0) {
                        StageUtils.ShowAlert(Alert.AlertType.ERROR, errorMessage);
                        return;
                    }

                    StageUtils.ShowAlert(Alert.AlertType.INFORMATION, "La modifica non è stata effettuata perchè i dati non sono consistenti, rieffetuare le modifiche...");

                    linkedMenu = ((ListResponse<Menu>) response).getPayload().get(0);

                    updateData();
                    return;
                }

                stageController.requestClose();
            }
        });
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
