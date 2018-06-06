package com.polimi.childcare.client.ui.controllers.stages.anagrafica;

import com.polimi.childcare.client.shared.networking.ClientNetworkManager;
import com.polimi.childcare.client.shared.networking.NetworkOperation;
import com.polimi.childcare.client.ui.constants.ToolbarButtons;
import com.polimi.childcare.client.ui.controllers.BaseStageController;
import com.polimi.childcare.client.ui.controllers.ChildcareBaseStageController;
import com.polimi.childcare.client.ui.controllers.ISceneController;
import com.polimi.childcare.client.ui.controllers.ISubSceneController;
import com.polimi.childcare.client.ui.controllers.stages.networking.BlockingNetworkOperationStageController;
import com.polimi.childcare.client.ui.controls.DragAndDropTableView;
import com.polimi.childcare.client.ui.controls.LabelTextViewComponent;
import com.polimi.childcare.client.ui.utils.StageUtils;
import com.polimi.childcare.shared.entities.*;
import com.polimi.childcare.shared.networking.requests.BaseRequest;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredContattoRequest;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredFornitoriRequest;
import com.polimi.childcare.shared.networking.requests.setters.SetContattoRequest;
import com.polimi.childcare.shared.networking.requests.setters.SetFornitoreRequest;
import com.polimi.childcare.shared.networking.requests.setters.SetPediatraRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListContattoResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListFornitoriResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListResponse;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Modality;

import java.io.IOException;
import java.util.Optional;

public class EditFornitoreStageController implements ISubSceneController
{
    public static final String FXML_PATH = "fxml/stages/anagrafica/EditFornitoreStage.fxml";

    private NetworkOperation pendingOperation;

    private Fornitore linkedFornitore;
    private ChildcareBaseStageController stageController;
    private Parent root;

    @FXML private AnchorPane rootPane;
    @FXML private AnchorPane loadingLayout;

    @FXML private TabPane layoutTabPane;
    @FXML private Tab tabDettagli;
    @FXML private Tab tabTelefoni;
    @FXML private Tab tabFax;

    @FXML private LabelTextViewComponent txtRagioneSociale;
    @FXML private LabelTextViewComponent txtPartitaIVA;
    @FXML private LabelTextViewComponent txtSedeLegale;
    @FXML private LabelTextViewComponent txtNumeroRegistroImprese;
    @FXML private LabelTextViewComponent txtEmail;
    @FXML private LabelTextViewComponent txtIBAN;

    @FXML private ListView<String> listTelefoni;
    @FXML private Button btnResetNumeri;

    @FXML private ListView<String> listFax;
    @FXML private Button btnResetFax;

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

            if(args != null && args.length > 0 && args[0] instanceof Fornitore)
                this.linkedFornitore = (Fornitore) args[0];


            if(pendingOperation == null && linkedFornitore != null && linkedFornitore.getID() != 0)
            {
                this.loadingLayout.setVisible(true);
                this.pendingOperation = new NetworkOperation(new FilteredFornitoriRequest(this.linkedFornitore.getID(), true),
                        response ->
                        {
                            this.pendingOperation = null;
                            if(!(response instanceof ListFornitoriResponse) || ((ListFornitoriResponse)response).getPayload().size() == 0)
                            {
                                StageUtils.ShowAlert(Alert.AlertType.ERROR, "Errore, risposta non corretta dal server");
                                stageController.requestClose();
                            }
                            else
                            {
                                this.linkedFornitore = ((ListFornitoriResponse)response).getPayload().get(0);
                                updateData();
                                this.loadingLayout.setVisible(false);
                            }
                        },
                        true);
                ClientNetworkManager.getInstance().submitOperation(this.pendingOperation);
            }

            //Tabelle Generali
            printDetails();
            setupPhoneList();

            //Bottoni
            if(linkedFornitore.getID() == 0) //Nascondo per persone nuove
                btnElimina.setVisible(false);

            btnElimina.setOnMouseClicked(click -> {
                if(this.pendingOperation == null)
                {
                    Optional<ButtonType> result = StageUtils.ShowAlertWithButtons(Alert.AlertType.CONFIRMATION,
                            "Sei sicuro di voler cancellare " + linkedFornitore.getRagioneSociale() + "?",
                            ButtonType.YES, ButtonType.NO);

                    if(result.isPresent() && result.get().equals(ButtonType.YES))
                    {
                        //Confermata la cancellazione
                        ShowBlockingNetworkOperationStage(new NetworkOperation(new SetFornitoreRequest(linkedFornitore, true, linkedFornitore.consistecyHashCode()),null, true), (resultArgs) -> {
                           if(resultArgs != null && resultArgs.length > 0 && resultArgs[0] instanceof BaseResponse)
                           {
                               BaseResponse response = (BaseResponse)resultArgs[0];
                               if(!(response instanceof BadRequestResponse))
                               {
                                   stageController.close();
                                   return;
                               }

                               if(response instanceof BadRequestResponse.BadRequestResponseWithMessage)
                                   StageUtils.ShowAlert(Alert.AlertType.ERROR, ((BadRequestResponse.BadRequestResponseWithMessage)response).getMessage());
                               else
                                   StageUtils.ShowAlert(Alert.AlertType.ERROR, "Errore nella cancellazione del fornitore");
                           }
                        });
                    }
                }
            });

            btnSalva.setOnMouseClicked(this::SaveClicked);

            //Aggiorna l'interfaccia con i presenti in linkedContatto
            updateData();
        }
    }

    private void updateData()
    {
        //Telefoni
        if(this.linkedFornitore != null && listTelefoni != null && this.linkedFornitore.getTelefoni() != null)
        {
            listTelefoni.getItems().clear();
            listTelefoni.getItems().addAll(this.linkedFornitore.getTelefoni());
        }

        //Fax
        if(this.linkedFornitore != null && listFax != null && this.linkedFornitore.getFax() != null)
        {
            listFax.getItems().clear();
            listFax.getItems().addAll(this.linkedFornitore.getFax());
        }
    }

    private void setupPhoneList()
    {
        listTelefoni.setCellFactory(TextFieldListCell.forListView());

        listTelefoni.setOnEditCommit(t -> {
            if(!t.getNewValue().isEmpty())
                listTelefoni.getItems().set(t.getIndex(), t.getNewValue());
            else
                listTelefoni.getItems().remove(t.getIndex());
        });

        listTelefoni.setOnMousePressed(click -> {
            if(click.isPrimaryButtonDown() && click.getClickCount() == 2)
            {
                if(listTelefoni.getSelectionModel().getSelectedItem() == null)
                    listTelefoni.getItems().add("+39");
            }
        });

        listFax.setCellFactory(TextFieldListCell.forListView());

        listFax.setOnEditCommit(t -> {
            if(!t.getNewValue().isEmpty())
                listFax.getItems().set(t.getIndex(), t.getNewValue());
            else
                listFax.getItems().remove(t.getIndex());
        });

        listFax.setOnMousePressed(click -> {
            if(click.isPrimaryButtonDown() && click.getClickCount() == 2)
            {
                if(listFax.getSelectionModel().getSelectedItem() == null)
                    listFax.getItems().add("+39");
            }
        });

        btnResetNumeri.setOnMouseClicked(click -> {
            listTelefoni.getItems().clear();
            listTelefoni.getItems().addAll(linkedFornitore.getTelefoni());
        });

        btnResetFax.setOnMouseClicked(click -> {
            listFax.getItems().clear();
            listFax.getItems().addAll(linkedFornitore.getFax());
        });
    }

    private void printDetails()
    {
        if(this.linkedFornitore != null)
        {
            //Setup title
            if(this.linkedFornitore.getRagioneSociale() != null)
                this.stageController.requestSetTitle(linkedFornitore.getRagioneSociale());
            else
                this.stageController.requestSetTitle("Crea " + linkedFornitore.getClass().getSimpleName());

            //Setup fields
            txtRagioneSociale.setTextFieldText(linkedFornitore.getRagioneSociale());
            txtPartitaIVA.setTextFieldText(linkedFornitore.getPartitaIVA());
            txtSedeLegale.setTextFieldText(linkedFornitore.getSedeLegale());
            txtNumeroRegistroImprese.setTextFieldText(linkedFornitore.getNumeroRegistroImprese());
            txtEmail.setTextFieldText(linkedFornitore.getEmail());
            txtIBAN.setTextFieldText(linkedFornitore.getIBAN());
        }
    }

    @Override
    public void detached()
    {
        if(pendingOperation != null)
            ClientNetworkManager.getInstance().abortOperation(this.pendingOperation);
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
        if(pendingOperation != null)
            return;

        Fornitore newFornitore = new Fornitore();

        //Imposta dettagli
        newFornitore.unsafeSetID(linkedFornitore.getID());
        newFornitore.setRagioneSociale(txtRagioneSociale.getTextFieldText());
        newFornitore.setPartitaIVA(txtPartitaIVA.getTextFieldText());
        newFornitore.setSedeLegale(txtSedeLegale.getTextFieldText());
        newFornitore.setNumeroRegistroImprese(txtNumeroRegistroImprese.getTextFieldText());
        newFornitore.setEmail(txtEmail.getTextFieldText());
        newFornitore.setIBAN(txtIBAN.getTextFieldText());

        for(String telefono : listTelefoni.getItems())
            newFornitore.addTelefono(telefono);


        for(String fax : listFax.getItems())
            newFornitore.addFax(fax);

        //Mantiene i mezzi e pasti
        for(Pasto pasto : linkedFornitore.getPasti())
            newFornitore.unsafeAddPasto(pasto);

        for(MezzoDiTrasporto mezzo : linkedFornitore.getMezzi())
            newFornitore.unsafeAddMezzo(mezzo);


        //Manda richiesta
        ShowBlockingNetworkOperationStage(new NetworkOperation(new SetFornitoreRequest(newFornitore, false, linkedFornitore.consistecyHashCode()), null, true), returnArgs ->
        {
            if(returnArgs != null && returnArgs.length > 0 && returnArgs[0] instanceof BaseResponse)
            {
                BaseResponse response = (BaseResponse)returnArgs[0];
                String errorMessage = "Impossibile eseguire l'operazione di modifica/inserimento, si è verificato un errore sconosciuto.";

                if(StageUtils.HandleResponseError(response, errorMessage, p -> p.getCode() == 200 || response instanceof ListResponse))
                    return;

                if(response instanceof ListResponse)
                {
                    if(((ListResponse) response).getPayload() == null || ((ListResponse) response).getPayload().size() == 0) {
                        StageUtils.ShowAlert(Alert.AlertType.ERROR, errorMessage);
                        return;
                    }

                    StageUtils.ShowAlert(Alert.AlertType.INFORMATION, "La modifica non è stata effettuata perchè i dati non sono consistenti, rieffetuare le modifiche...");

                    this.linkedFornitore = ((ListResponse<Fornitore>) response).getPayload().get(0);
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
