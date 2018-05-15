package com.polimi.childcare.client.ui.controllers;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class BaseStageController implements IStageController
{
    protected Stage linkedStage;

    private String title;

    //Class constant properties
    public String getTitle() { return title; }

    public void setTitle(String title)
    {
        this.title = title;
        if(this.linkedStage != null)
            this.linkedStage.setTitle(title);
    }

    public double getControllerWidth() { return 100; }
    public double getControllerHeight() { return 100; }
    public boolean isResizable() { return true; }

    @Override
    public Scene setupScene(Parent parent)
    {
        Scene scene = new Scene(parent, this.getControllerWidth(), this.getControllerHeight());
        this.linkedStage.setScene(scene);
        return scene;
    }

    @Override
    public Scene setupStageAndShow(Stage stage, Parent parent)
    {
        this.linkedStage = stage;
        this.linkedStage.setTitle(this.getTitle());
        this.linkedStage.setResizable(this.isResizable());
        this.linkedStage.show();

        return setupScene(parent);
    }
}
