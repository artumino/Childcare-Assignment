package com.polimi.childcare.client.ui.controllers.stages.presenze;

import com.polimi.childcare.client.shared.networking.NetworkOperation;
import com.polimi.childcare.client.ui.constants.ToolbarButtons;
import com.polimi.childcare.client.ui.controllers.ChildcareBaseStageController;
import com.polimi.childcare.client.ui.controllers.ISceneController;
import com.polimi.childcare.client.ui.controllers.ISubSceneController;
import com.polimi.childcare.client.ui.controllers.subscenes.NetworkedSubScene;
import com.polimi.childcare.client.ui.utils.StageUtils;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.entities.RegistroPresenze;
import com.polimi.childcare.shared.networking.requests.setters.SetRegistroPresenzeRequest;
import com.polimi.childcare.shared.networking.requests.special.SetPresenzaRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListRegistroPresenzeResponse;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;

public class SetPresenzaStage extends NetworkedSubScene implements ISubSceneController
{
    private Parent root;

    @FXML private AnchorPane rootPane;
    @FXML private Button btnEnter;
    @FXML private Button btnExit;
    @FXML private ComboBoxBase<LocalTime> pickerEventHour;
    @FXML private AnchorPane loadingLayout;
    @FXML private ComboBox<String> cbStatoPresenza;

    private ChildcareBaseStageController stageController;
    private RegistroPresenze linkedRegistroPresenza;

    @Override
    public void attached(ISceneController sceneController, Object... args)
    {
        if(sceneController instanceof ChildcareBaseStageController)
            this.stageController = (ChildcareBaseStageController)sceneController;


        if(this.stageController != null)
        {
            this.stageController.setToolbarButtonsVisibilityMask((byte)(ToolbarButtons.Close | ToolbarButtons.Minimize));

            if(args.length == 0 || !(args[0] instanceof RegistroPresenze)) {
                this.stageController.close();
                return;
            }

            this.linkedRegistroPresenza = (RegistroPresenze)args[0];
            this.stageController.requestSetTitle("Aggiorna - " + linkedRegistroPresenza.getBambino().getNome() + " " + linkedRegistroPresenza.getBambino().getCognome());

            for(RegistroPresenze.StatoPresenza presenza : RegistroPresenze.StatoPresenza.values())
                this.cbStatoPresenza.getItems().add(presenza.toString());

            this.cbStatoPresenza.getSelectionModel().select(this.linkedRegistroPresenza.getStato().toString());

            this.btnEnter.setOnMouseClicked(event -> SendPresenzaEvent());
            this.btnExit.setOnMouseClicked(event -> this.stageController.requestClose());
            this.pickerEventHour.setValue(linkedRegistroPresenza.getTimeStamp().toLocalTime());
        }
    }

    private void SendPresenzaEvent()
    {
        if(this.pickerEventHour.getValue() != null && !networkOperationVault.anyRunningOperation())
        {
            this.stageController.lock();
            this.loadingLayout.setVisible(true);

            RegistroPresenze newPresenza = new RegistroPresenze();
            newPresenza.setBambino(linkedRegistroPresenza.getBambino());
            newPresenza.setGita(linkedRegistroPresenza.getGita());
            newPresenza.setDate(linkedRegistroPresenza.getDate());
            newPresenza.setTimeStamp(linkedRegistroPresenza.getDate().atTime(pickerEventHour.getValue()));
            newPresenza.setOra((short)pickerEventHour.getValue().getHour());
            newPresenza.setStato(Enum.valueOf(RegistroPresenze.StatoPresenza.class, cbStatoPresenza.getSelectionModel().getSelectedItem()));

            networkOperationVault.submitOperation(new NetworkOperation(new SetRegistroPresenzeRequest(newPresenza,
                    false,
                    linkedRegistroPresenza.consistecyHashCode()),
                    this::ProcessSendPresenzaResponse, true));
        }
    }

    private void ProcessSendPresenzaResponse(BaseResponse response)
    {
        networkOperationVault.operationDone(SetPresenzaRequest.class);

        if(StageUtils.HandleResponseError(response, "Errore nell'aggiornare i dati", p -> p.getCode() == 200))
            return;

        this.stageController.unlock();
        this.stageController.requestClose();
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
