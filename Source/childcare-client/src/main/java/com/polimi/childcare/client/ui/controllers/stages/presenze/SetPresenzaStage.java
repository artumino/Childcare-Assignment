package com.polimi.childcare.client.ui.controllers.stages.presenze;

import com.polimi.childcare.client.shared.networking.ClientNetworkManager;
import com.polimi.childcare.client.shared.networking.NetworkOperation;
import com.polimi.childcare.client.ui.constants.ToolbarButtons;
import com.polimi.childcare.client.ui.controllers.ChildcareBaseStageController;
import com.polimi.childcare.client.ui.controllers.ISceneController;
import com.polimi.childcare.client.ui.controllers.ISubSceneController;
import com.polimi.childcare.client.ui.utils.StageUtils;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.networking.requests.setters.SetPresenzaRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListRegistroPresenzeResponse;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;

public class SetPresenzaStage implements ISubSceneController
{
    private Parent root;
    @FXML private AnchorPane rootPane;
    @FXML private Button btnEnter;
    @FXML private Button btnExit;
    @FXML private ComboBoxBase<LocalTime> pickerEventHour;
    @FXML private AnchorPane loadingLayout;

    private ChildcareBaseStageController stageController;
    private Bambino linkedBambino;
    private NetworkOperation pendingOperation;

    @Override
    public void attached(ISceneController sceneController, Object... args)
    {
        if(sceneController instanceof ChildcareBaseStageController)
            this.stageController = (ChildcareBaseStageController)sceneController;


        if(this.stageController != null)
        {
            this.stageController.setToolbarButtonsVisibilityMask((byte)(ToolbarButtons.Close | ToolbarButtons.Minimize));

            if(args.length == 0 || !(args[0] instanceof Bambino)) {
                this.stageController.requestClose();
                return;
            }

            this.linkedBambino = (Bambino)args[0];
            this.stageController.requestSetTitle("Aggiorna - " + linkedBambino.getNome() + " " + linkedBambino.getCognome());

            this.btnEnter.setOnMouseClicked(event -> SendPresenzaEvent(false));
            this.btnExit.setOnMouseClicked(event -> SendPresenzaEvent(true));
            this.pickerEventHour.setValue(LocalDateTime.now().toLocalTime());
        }
    }

    private void SendPresenzaEvent(boolean uscita)
    {
        if(this.pickerEventHour.getValue() != null && pendingOperation == null)
        {

            this.pendingOperation = new NetworkOperation(new SetPresenzaRequest(linkedBambino.getID(),
                    0,
                    false,
                    this.pickerEventHour.getValue().atDate(LocalDate.now()).toEpochSecond(ZoneOffset.UTC),
                    uscita), this::ProcessSendPresenzaResponse, true);
            this.stageController.lock();
            this.loadingLayout.setVisible(true);
            ClientNetworkManager.getInstance().submitOperation(this.pendingOperation);
        }
    }

    private void ProcessSendPresenzaResponse(BaseResponse response)
    {
        this.pendingOperation = null;
        if(!(response instanceof ListRegistroPresenzeResponse))
            StageUtils.ShowAlert(Alert.AlertType.ERROR, "Errore nell'aggiornare i dati: " + (response != null ? "Bad Request" : "Risposta Nulla"));
        else
        {
            ListRegistroPresenzeResponse data = (ListRegistroPresenzeResponse)response;
            if(data.getPayload().size() > 0)
                this.stageController.requestClose(data.getPayload().get(0));
        }
        this.stageController.unlock();
        this.loadingLayout.setVisible(false);
    }

    @Override
    public void detached()
    {

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
