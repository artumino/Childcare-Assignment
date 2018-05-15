package com.polimi.childcare.client.ui.controllers;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//Container in grado di impostare la propria Scene tramite il metodo setupScene()
public interface ISceneController
{
    Scene setupScene(Stage stage, Parent parent);
}
