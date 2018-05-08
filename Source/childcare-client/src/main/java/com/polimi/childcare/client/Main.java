package com.polimi.childcare.client;

import com.polimi.childcare.client.ui.controllers.StartingBoxController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.rmi.RMISecurityManager;

public class Main extends Application
{

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader();
        StackPane pane = loader.load(getClass().getResource("/fxml/StartingBox.fxml").openStream());
        StartingBoxController controller = loader.getController();
        controller.setup(primaryStage, pane);
        primaryStage.show();
        //Platform.exit();
    }


    public static void main(String[] args)
    {
        System.setProperty("java.security.policy", "resources/security.policy");
        launch(args);
    }
}
