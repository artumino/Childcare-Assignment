package com.polimi.childcare.client.ui.utils;

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
}
