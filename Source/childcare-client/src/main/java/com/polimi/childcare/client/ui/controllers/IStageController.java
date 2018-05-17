package com.polimi.childcare.client.ui.controllers;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//Imposta la propria scene, imposta il proprio stage, mostra lo stage
public interface IStageController extends ISceneController
{
    Scene setupStageAndShow(Stage stage, Parent parent);
    void requestSetTitle(String newTitle);
    void requestClose();
}
