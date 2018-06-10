package com.polimi.childcare.client.ui.controllers.stages.gita;

import com.polimi.childcare.client.shared.networking.NetworkOperation;
import com.polimi.childcare.client.ui.constants.ToolbarButtons;
import com.polimi.childcare.client.ui.controllers.ChildcareBaseStageController;
import com.polimi.childcare.client.ui.controllers.ISceneController;
import com.polimi.childcare.client.ui.controllers.ISubSceneController;
import com.polimi.childcare.client.ui.controllers.subscenes.NetworkedSubScene;
import com.polimi.childcare.client.ui.controls.LabelTextViewComponent;
import com.polimi.childcare.client.ui.utils.StageUtils;
import com.polimi.childcare.shared.entities.*;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredGitaRequest;
import com.polimi.childcare.shared.networking.requests.setters.SetGitaRequest;
import com.polimi.childcare.shared.networking.requests.setters.SetMezzoDiTrasportoRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListGitaResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListResponse;
import com.polimi.childcare.shared.utils.EntitiesHelper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EditGitaStageController extends NetworkedSubScene implements ISubSceneController
{
    public static final String FXML_PATH = "fxml/stages/gita/EditGitaStage.fxml";

    private Gita linkedGita;
    private ChildcareBaseStageController stageController;
    private Parent root;

    @FXML private AnchorPane rootPane;
    @FXML private AnchorPane loadingLayout;

    @FXML private TabPane layoutTabPane;

    @FXML private LabelTextViewComponent txtLuogo;
    @FXML private LabelTextViewComponent txtCosto;
    @FXML private DatePicker dpDataInizio;
    @FXML private DatePicker dpDataFine;

    //Tappe
    @FXML private ListView<Tappa> listTappe;
    @FXML private Button btnAddTappa;
    @FXML private Button btnRemoveTappa;

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

            if(args != null && args.length > 0 && args[0] instanceof Gita)
                this.linkedGita = (Gita) args[0];


            if(linkedGita != null && linkedGita.getID() != 0)
            {
                this.loadingLayout.setVisible(true);
                networkOperationVault.submitOperation(new NetworkOperation(new FilteredGitaRequest(this.linkedGita.getID(), true),
                        response ->
                        {
                            networkOperationVault.operationDone(FilteredGitaRequest.class);

                            if(StageUtils.HandleResponseError(response, "Errore, risposta non corretta dal server",
                                    p -> p instanceof ListGitaResponse && ((ListGitaResponse)p).getPayload().size() != 0))
                                stageController.requestClose();
                            else
                            {
                                this.linkedGita = ((ListGitaResponse)response).getPayload().get(0);
                                updateData();
                                this.loadingLayout.setVisible(false);
                            }
                        },
                        true));
            }

            //Tabelle Generali
            printGitaDetails();
            setupListTappe();

            //Bottoni
            if(linkedGita.getID() == 0) //Nascondo per persone nuove
                btnElimina.setVisible(false);

            btnElimina.setOnMouseClicked(click -> {
                    Optional<ButtonType> result = StageUtils.ShowAlertWithButtons(Alert.AlertType.CONFIRMATION,
                            "Sei sicuro di voler cancellare " + linkedGita.getLuogo() + "?",
                            ButtonType.YES, ButtonType.NO);

                    if(result.isPresent() && result.get().equals(ButtonType.YES))
                    {
                        //Confermata la cancellazione
                        ShowBlockingNetworkOperationStage(new NetworkOperation(new SetGitaRequest(linkedGita, true, linkedGita.consistecyHashCode()),null, true), (resultArgs) -> {
                           if(resultArgs != null && resultArgs.length > 0 && resultArgs[0] instanceof BaseResponse)
                           {
                               BaseResponse response = (BaseResponse)resultArgs[0];

                               if(!(response instanceof BadRequestResponse))
                               {
                                   stageController.close();
                                   return;
                               }

                               StageUtils.HandleResponseError(response, "Errore nella cancellazione della gita", p -> true);
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
        //Tappe
        if(this.linkedGita != null && listTappe != null && linkedGita.getTappe() != null)
            listTappe.getItems().addAll(linkedGita.getTappe());

        printGitaDetails();
    }

    private void setupListTappe()
    {
        if(listTappe != null)
        {
            listTappe.setCellFactory(TextFieldListCell.forListView(new StringConverter<Tappa>() {
                @Override
                public String toString(Tappa object) {
                    return object.getLuogo();
                }

                @Override
                public Tappa fromString(String string) {
                    return new Tappa(string, linkedGita);
                }
            }));

            btnAddTappa.setOnMouseClicked(click -> listTappe.getItems().add(new Tappa("", linkedGita)));

            btnRemoveTappa.setOnMouseClicked(click -> {
                if (listTappe.getSelectionModel().getSelectedItem() != null)
                    listTappe.getItems().remove(listTappe.getSelectionModel().getSelectedItem());
            });
        }
    }

    private void printGitaDetails()
    {

        if(this.linkedGita != null)
        {
            //Setup title
            if(this.linkedGita.getLuogo() != null)
                this.stageController.requestSetTitle(linkedGita.getLuogo());
            else
                this.stageController.requestSetTitle("Crea " + linkedGita.getClass().getSimpleName());

            //Setup fields
            txtLuogo.setTextFieldText(linkedGita.getLuogo());
            txtCosto.setTextFieldText(String.valueOf(linkedGita.getCosto()));
            dpDataInizio.setValue(linkedGita.getDataInizio() != null ? linkedGita.getDataInizio() : LocalDate.now());
            dpDataFine.setValue(linkedGita.getDataFine() != null ? linkedGita.getDataFine() : LocalDate.now());
        }
    }

    private void SaveClicked(MouseEvent ignored)
    {
        Gita newGita = new Gita();

        //Imposta dettagli
        newGita.unsafeSetID(linkedGita.getID());
        newGita.setLuogo(txtLuogo.getTextFieldText());

        try
        {
            newGita.setCosto(Integer.parseInt(txtCosto.getTextFieldText()));
        }
        catch (Exception ex)
        {
            StageUtils.ShowAlert(Alert.AlertType.ERROR, "Alcuni parametri numerici hanno valori non numerici!");
            return;
        }

        newGita.setDataInizio(dpDataInizio.getValue());
        newGita.setDataFine(dpDataFine.getValue());

        for (Tappa tappa : listTappe.getItems())
            newGita.unsafeAddTappa(tappa);

        for(RegistroPresenze registroPresenze : linkedGita.getRegistriPresenze())
            newGita.unsafeAddRegistroPresenza(registroPresenze);

        for(PianoViaggi pianoViaggi : linkedGita.getPianiViaggi())
            newGita.unsafeAddPianoViaggi(pianoViaggi);

        //Manda richiesta
        ShowBlockingNetworkOperationStage(new NetworkOperation(new SetGitaRequest(newGita, false, linkedGita.consistecyHashCode()), null, true), returnArgs ->
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

                    linkedGita = ((ListResponse<Gita>) response).getPayload().get(0);

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
