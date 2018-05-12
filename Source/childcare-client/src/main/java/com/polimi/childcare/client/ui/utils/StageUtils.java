package com.polimi.childcare.client.ui.utils;

import com.polimi.childcare.client.ui.controllers.BaseStageController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class StageUtils
{
    public static <T extends BaseStageController> T showGenericStage(Stage stage, String fxmlPath) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        Parent pane = loader.load(StageUtils.class.getResource(fxmlPath).openStream());
        T controller = loader.getController();
        controller.setupStageAndShow(stage, pane);
        return controller;
    }

    public static <T extends BaseStageController> T showGenericStage(String fxmlPath) throws IOException
    {
        Stage newStage = new Stage();
        return showGenericStage(newStage, fxmlPath);
    }

    public static BaseStageController showStage(String fxmlPath) throws IOException
    {
        return StageUtils.showGenericStage(fxmlPath);
    }

    public static BaseStageController showStage(Stage stage, String fxmlPath) throws IOException
    {
        return StageUtils.showGenericStage(stage, fxmlPath);
    }
}
