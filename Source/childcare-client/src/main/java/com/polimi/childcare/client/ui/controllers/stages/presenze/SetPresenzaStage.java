package com.polimi.childcare.client.ui.controllers.stages.presenze;

import com.polimi.childcare.client.ui.constants.ToolbarButtons;
import com.polimi.childcare.client.ui.controllers.ChildcareBaseStageController;
import com.polimi.childcare.client.ui.controllers.ISceneController;
import com.polimi.childcare.client.ui.controllers.IStageController;
import com.polimi.childcare.client.ui.controllers.ISubSceneController;
import com.polimi.childcare.shared.entities.Bambino;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

public class SetPresenzaStage implements ISubSceneController
{
    private Parent root;
    @FXML private AnchorPane rootPane;
    @FXML private Button btnClose;

    private IStageController stageController;
    private Bambino linkedBambino;

    @Override
    public void attached(ISceneController sceneController, Object... args)
    {
        if(sceneController instanceof IStageController)
            this.stageController = (IStageController)sceneController;


        if(this.stageController != null)
        {
            if(this.stageController instanceof ChildcareBaseStageController)
                ((ChildcareBaseStageController)this.stageController).setToolbarButtonsVisibilityMask((byte)(ToolbarButtons.Close | ToolbarButtons.Minimize));
            if(args.length == 0 || !(args[0] instanceof Bambino)) {
                this.stageController.requestClose();
                return;
            }

            this.linkedBambino = (Bambino)args[0];
            this.stageController.requestSetTitle("Aggiorna - " + linkedBambino.getNome() + " " + linkedBambino.getCognome());
        }

        btnClose.setOnMouseClicked(event -> stageController.requestClose());
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
