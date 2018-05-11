package com.polimi.childcare.client.ui.controllers;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

//Container in grado di impostare la propria Scene tramite il metodo setup()
public interface ISelfController
{
    Scene setup(Stage stage, Parent parent);
}
