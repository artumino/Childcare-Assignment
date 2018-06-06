package com.polimi.childcare.client.ui.controllers.subscenes.submenus;

import com.polimi.childcare.client.ui.controllers.ISubSceneController;
import com.polimi.childcare.client.ui.controllers.subscenes.NetworkedSubScene;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

public abstract class SubMenuBase extends NetworkedSubScene implements ISubSceneController
{
    private Parent root;

    @FXML private AnchorPane rootPane;

    @FXML
    protected abstract void initialize();

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
