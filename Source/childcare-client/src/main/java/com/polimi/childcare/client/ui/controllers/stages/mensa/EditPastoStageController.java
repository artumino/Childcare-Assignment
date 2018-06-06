package com.polimi.childcare.client.ui.controllers.stages.mensa;

import com.polimi.childcare.client.shared.networking.NetworkOperation;
import com.polimi.childcare.client.ui.constants.ToolbarButtons;
import com.polimi.childcare.client.ui.controllers.BaseStageController;
import com.polimi.childcare.client.ui.controllers.ChildcareBaseStageController;
import com.polimi.childcare.client.ui.controllers.ISceneController;
import com.polimi.childcare.client.ui.controllers.ISubSceneController;
import com.polimi.childcare.client.ui.controllers.stages.generic.ReazioniAvverseStage;
import com.polimi.childcare.client.ui.controllers.stages.networking.BlockingNetworkOperationStageController;
import com.polimi.childcare.client.ui.controllers.subscenes.NetworkedSubScene;
import com.polimi.childcare.client.ui.controls.DragAndDropTableView;
import com.polimi.childcare.client.ui.controls.LabelTextViewComponent;
import com.polimi.childcare.client.ui.utils.StageUtils;
import com.polimi.childcare.shared.entities.*;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredPastoRequest;
import com.polimi.childcare.shared.networking.requests.setters.SetPastiRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListPastiResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListResponse;
import com.polimi.childcare.shared.utils.EntitiesHelper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Modality;

import java.io.IOException;
import java.util.Optional;

public class EditPastoStageController extends NetworkedSubScene implements ISubSceneController
{
    public static final String FXML_PATH = "fxml/stages/mensa/EditPastoStage.fxml";

    private Pasto linkedPasto;
    private ChildcareBaseStageController stageController;
    private Parent root;

    @FXML private AnchorPane rootPane;
    @FXML private AnchorPane loadingLayout;

    @FXML private TabPane layoutTabPane;

    @FXML private LabelTextViewComponent txtNome;
    @FXML private TextArea txtDescrizione;
    @FXML private DragAndDropTableView<Fornitore> tableFornitore;

    @FXML private DragAndDropTableView<ReazioneAvversa> tableReazioneAvverse;
    @FXML private Button btnShowReazioniAvverse;

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

            if(args != null && args.length > 0 && args[0] instanceof Pasto)
                this.linkedPasto = (Pasto)args[0];


            if(linkedPasto != null && linkedPasto.getID() != 0)
            {
                this.loadingLayout.setVisible(true);
                networkOperationVault.submitOperation(new NetworkOperation(new FilteredPastoRequest(this.linkedPasto.getID(), true),
                        response ->
                        {
                            networkOperationVault.operationDone(FilteredPastoRequest.class);

                            if(StageUtils.HandleResponseError(response, "Errore, risposta non corretta dal server",
                                    p -> p instanceof ListPastiResponse && ((ListPastiResponse)p).getPayload().size() != 0))
                                stageController.requestClose();
                            else
                            {
                                this.linkedPasto = ((ListPastiResponse)response).getPayload().get(0);
                                updateData();
                                this.loadingLayout.setVisible(false);
                            }
                        },
                        true));
            }

            //Tabelle Generali
            printPastoDetails();
            setupReazioniAvverse();
            setupTableFornitori();

            //Bottoni
            if(linkedPasto.getID() == 0) //Nascondo per persone nuove
                btnElimina.setVisible(false);

            btnElimina.setOnMouseClicked(click -> {
                    Optional<ButtonType> result = StageUtils.ShowAlertWithButtons(Alert.AlertType.CONFIRMATION,
                            "Sei sicuro di voler cancellare " + linkedPasto.getNome() + "?",
                            ButtonType.YES, ButtonType.NO);

                    if(result.isPresent() && result.get().equals(ButtonType.YES))
                    {
                        //Confermata la cancellazione
                        ShowBlockingNetworkOperationStage(new NetworkOperation(new SetPastiRequest(linkedPasto, true, linkedPasto.consistecyHashCode()),null, true), (resultArgs) -> {
                           if(resultArgs != null && resultArgs.length > 0 && resultArgs[0] instanceof BaseResponse)
                           {
                               BaseResponse response = (BaseResponse)resultArgs[0];

                               if(!(response instanceof BadRequestResponse))
                               {
                                   stageController.close();
                                   return;
                               }

                               StageUtils.HandleResponseError(response, "Errore nella cancellazione del pasto", p -> true);
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
        //Fornitore
        if(linkedPasto != null && tableFornitore != null && linkedPasto.getFornitore() != null)
        {
            tableFornitore.getItems().clear();
            tableFornitore.getItems().add(linkedPasto.getFornitore());
        }

        //Reazioni Avverse
        if(this.linkedPasto != null && tableReazioneAvverse != null && linkedPasto.getReazione() != null)
        {
            tableReazioneAvverse.getItems().clear();
            tableReazioneAvverse.getItems().addAll(linkedPasto.getReazione());
        }

        printPastoDetails();
    }

    private void setupReazioniAvverse()
    {
        TableColumn<ReazioneAvversa, String> cNome = new TableColumn<>("Nome");
        cNome.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getNome()));


        if(tableReazioneAvverse != null)
        {
            tableReazioneAvverse.getColumns().addAll(cNome);
            btnShowReazioniAvverse.setOnMouseClicked(click -> ShowReazioniAvverse());
            tableReazioneAvverse.dragForClass(ReazioneAvversa.class);
        }
    }

    private void setupTableFornitori()
    {
        TableColumn<Fornitore, String> cRagioneSociale = new TableColumn<>("Ragione Sociale");
        TableColumn<Fornitore, String> cSedeLegale = new TableColumn<>("Sede Legale");
        TableColumn<Fornitore, String> cTelefoni = new TableColumn<>("Telefoni");
        cRagioneSociale.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getRagioneSociale()));
        cSedeLegale.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getSedeLegale()));
        cTelefoni.setCellValueFactory(c -> new ReadOnlyStringWrapper(EntitiesHelper.getTelefoniStringFromIterable(c.getValue().getTelefoni())));


        if(tableFornitore != null)
        {
            tableFornitore.getColumns().addAll(cRagioneSociale, cSedeLegale, cTelefoni);
            btnShowReazioniAvverse.setOnMouseClicked(click -> ShowReazioniAvverse());
            tableFornitore.dragForClass(Fornitore.class);
        }
    }

    private void printPastoDetails()
    {

        if(this.linkedPasto != null)
        {
            //Setup title
            if(this.linkedPasto.getNome() != null)
                this.stageController.requestSetTitle(linkedPasto.getNome());
            else
                this.stageController.requestSetTitle("Crea " + linkedPasto.getClass().getSimpleName());

            //Setup fields
            txtNome.setTextFieldText(linkedPasto.getNome());
            txtDescrizione.setText(linkedPasto.getDescrizione());
        }
    }

    private void ShowReazioniAvverse()
    {
        try {
            ChildcareBaseStageController showReazioniAvverse = new ChildcareBaseStageController();
            showReazioniAvverse.setContentScene(getClass().getClassLoader().getResource(ReazioniAvverseStage.FXML_PATH));
            showReazioniAvverse.initOwner(getRoot().getScene().getWindow());
            //showReazioniAvverse.initModality(Modality.APPLICATION_MODAL); //Blocco tutto
            showReazioniAvverse.setOnClosingCallback((returnArgs) -> {
                //Niente
            });
            showReazioniAvverse.show();
        } catch (IOException e) {
            e.printStackTrace();
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

    private void SaveClicked(MouseEvent ignored)
    {
        Pasto newPasto = new Pasto();

        //Imposta dettagli
        newPasto.unsafeSetID(linkedPasto.getID());
        newPasto.setNome(txtNome.getTextFieldText());
        newPasto.setDescrizione(txtDescrizione.getText());

        if(tableFornitore.getItems().size() > 0)
            newPasto.setFornitore(tableFornitore.getItems().get(0));

        //Imposta relazioni persona
        for(ReazioneAvversa reazioneAvversa : tableReazioneAvverse.getItems())
            newPasto.addReazione(reazioneAvversa);

        //Manda richiesta
        ShowBlockingNetworkOperationStage(new NetworkOperation(new SetPastiRequest(newPasto, false, linkedPasto.consistecyHashCode()), null, true), returnArgs ->
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

                    linkedPasto = ((ListResponse<Pasto>) response).getPayload().get(0);

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
