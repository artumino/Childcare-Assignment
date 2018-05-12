package com.polimi.childcare.client.ui.controllers;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainMenuStageController extends BaseStageController
{
    @Override
    public String getTitle()
    {
        return "ChildCare Portal";
    }

    @Override
    public Scene setupStageAndShow(Stage stage, Parent parent)
    {
        stage.initStyle(StageStyle.UNDECORATED);
        Scene scene = super.setupStageAndShow(stage, parent);
        return scene;
    }
}
