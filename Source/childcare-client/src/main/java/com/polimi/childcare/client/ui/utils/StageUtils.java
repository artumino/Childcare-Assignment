package com.polimi.childcare.client.ui.utils;

import com.polimi.childcare.client.ui.controllers.BaseStageController;
import com.polimi.childcare.client.ui.controllers.ChildcareBaseStageController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class StageUtils
{
    public static <T extends BaseStageController> T showGenericStage(Stage stage, URL fxmlPath) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(fxmlPath);
        Parent pane = loader.load(fxmlPath.openStream());
        T controller = loader.getController();
        controller.setupStageAndShow(stage, pane);
        return controller;
    }

    public static <T extends BaseStageController> T showGenericStage(URL fxmlPath) throws IOException
    {
        Stage newStage = new Stage();
        return showGenericStage(newStage, fxmlPath);
    }

    public static ChildcareBaseStageController showChildcareStage(Stage stage, URL fxmlContentPath) throws IOException
    {
        ChildcareBaseStageController controller = showGenericStage(stage, StageUtils.class.getClassLoader().getResource("fxml/ChildcareBaseStage.fxml"));
        controller.setContentScene(fxmlContentPath);
        return controller;
    }

    public static ChildcareBaseStageController showChildcareStage(URL fxmlContentPath) throws IOException
    {
        Stage newStage = new Stage();
        return showChildcareStage(newStage, fxmlContentPath);
    }
    
    public static BaseStageController showStage(URL fxmlPath) throws IOException
    {
        return StageUtils.showGenericStage(fxmlPath);
    }

    public static BaseStageController showStage(Stage stage, URL fxmlPath) throws IOException
    {
        return StageUtils.showGenericStage(stage, fxmlPath);
    }
}
