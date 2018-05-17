package com.polimi.childcare.client.ui.utils;

import com.polimi.childcare.client.ui.controllers.ISubSceneController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;

public class SceneUtils
{
    public static Parent loadScene(URL fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(fxmlPath);
        return loader.load(fxmlPath.openStream());
    }

    public static ISubSceneController loadSubScene(URL fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(fxmlPath);
        Parent root = loader.load(fxmlPath.openStream());
        ISubSceneController controller = loader.getController();
        controller.setupScene(root);
        return controller;
    }
}
