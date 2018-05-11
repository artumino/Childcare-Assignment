package com.polimi.childcare.client.ui.controllers;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class BaseController implements ISelfController
{
    private Stage linkedStage;

    //Class constant properties
    public abstract String getTitle();
    public double getControllerWidth() { return 100; }
    public double getControllerHeight() { return 100; }
    public boolean isResizable() { return true; }

    @Override
    public Scene setup(Stage stage, Parent parent)
    {
        Scene scene = new Scene(parent, this.getControllerHeight(), this.getControllerWidth());
        this.linkedStage = stage;
        this.linkedStage.setTitle(this.getTitle());
        this.linkedStage.setResizable(this.isResizable());
        this.linkedStage.setScene(scene);
        return scene;
    }
}
