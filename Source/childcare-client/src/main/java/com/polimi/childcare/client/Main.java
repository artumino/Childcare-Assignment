package com.polimi.childcare.client;

import com.polimi.childcare.client.shared.networking.ClientNetworkManager;
import com.polimi.childcare.client.ui.controllers.BaseStageController;
import com.polimi.childcare.client.ui.controllers.ChildcareBaseStageController;
import com.polimi.childcare.client.ui.controllers.subscenes.StartingSubsceneController;
import com.polimi.childcare.client.ui.utils.StageUtils;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application
{

    @Override
    public void start(Stage ignored) throws Exception
    {
        ChildcareBaseStageController baseStage = new ChildcareBaseStageController();
        baseStage.setContentScene(this.getClass().getClassLoader().getResource(StartingSubsceneController.FXML_PATH));
        baseStage.show();
    }

    @Override
    public void stop()
    {
        //Ferma network manager
        ClientNetworkManager.getInstance().Dispose();

        System.exit(0);
    }

    public static void main(String[] args)
    {
        System.setProperty("java.security.policy", "resources/security.policy");
        launch(args);
    }
}
