package com.polimi.childcare.client.ui.controllers.stages.networking;

import com.polimi.childcare.client.shared.networking.ClientNetworkManager;
import com.polimi.childcare.client.shared.networking.NetworkOperation;
import com.polimi.childcare.client.ui.controllers.ChildcareBaseStageController;
import com.polimi.childcare.client.ui.controllers.ISceneController;
import com.polimi.childcare.client.ui.controllers.ISubSceneController;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Elemento dell'interfaccia utilizzato per bloccare l'applicazione fino alla terminazione di una operazione di rete
 * Chiamare con initModality(APPLICATION)
 */
public class BlockingNetworkOperationStageController implements ISubSceneController, NetworkOperation.INetworkOperationCallback
{
    public static final String FXML_PATH = "fxml/stages/networking/BlockingNetworkOperationStage.fxml";

    @FXML private AnchorPane paneRoot;
    @FXML private Label txtMessage;

    private ChildcareBaseStageController linkedStageController;
    private Timer uiTimer;
    private String startingText;

    @FXML
    protected void initialize()
    {
        this.uiTimer = new Timer();
        this.startingText = txtMessage.getText();

        //Animazione testo
        this.uiTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run()
            {
                Platform.runLater(() -> {
                    if(txtMessage.getText().endsWith("..."))
                        txtMessage.setText(startingText);
                    else
                        txtMessage.setText(txtMessage.getText() + ".");
                });
            }
        }, 0, 1000);
    }

    @Override
    public void attached(ISceneController sceneController, Object... args)
    {
        if(sceneController instanceof ChildcareBaseStageController)
        {
            this.linkedStageController = (ChildcareBaseStageController) sceneController;
            this.linkedStageController.requestSetTitle(""); //Niente titolo

            //Nessun bottone
            this.linkedStageController.setToolbarButtonsVisibilityMask((byte)0x00);

            if(args.length > 0 && args[0] instanceof NetworkOperation)
            {
                NetworkOperation networkOperation = (NetworkOperation) args[0];
                networkOperation.setCallback(this);
                networkOperation.setRunOnUiThread(true);
                ClientNetworkManager.getInstance().submitOperation(networkOperation);
            }
            else
                this.linkedStageController.requestClose();
        }
        else if(sceneController instanceof Stage)
            ((Stage)sceneController).close();
    }

    @Override
    public void detached()
    {
        //DO NOTHING...
        if(uiTimer != null)
            uiTimer.cancel();
    }

    @Override
    public Region getSceneRegion() {
        return paneRoot;
    }

    @Override
    public Parent getRoot() {
        return paneRoot;
    }

    @Override
    public Scene setupScene(Parent parent) {
        return paneRoot.getScene();
    }

    @Override
    public void OnResult(BaseResponse response)
    {
        //Mi chiudo e rimando la risposta al mio owner
        if(this.linkedStageController != null)
            this.linkedStageController.requestClose(response);
    }
}
