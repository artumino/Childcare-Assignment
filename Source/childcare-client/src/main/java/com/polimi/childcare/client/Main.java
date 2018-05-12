package com.polimi.childcare.client;

import com.polimi.childcare.client.networking.ClientNetworkManager;
import com.polimi.childcare.client.ui.utils.StageUtils;
import javafx.application.Application;
import javafx.stage.Stage;

import java.rmi.RMISecurityManager;

public class Main extends Application
{

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        StageUtils.showGenericStage(primaryStage, this.getClass().getResource("/fxml/StartingBox.fxml"));
        //Platform.exit();
    }

    @Override
    public void stop() throws Exception
    {
        //Ferma network manager
        ClientNetworkManager.getInstance().Dispose();

        super.stop();
    }

    public static void main(String[] args)
    {
        System.setProperty("java.security.policy", "resources/security.policy");
        launch(args);
    }
}
