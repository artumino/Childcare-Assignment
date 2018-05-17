package com.polimi.childcare.client.ui.controllers;

import javafx.scene.Parent;
import javafx.scene.layout.Region;

public interface ISubSceneController extends ISceneController
{
    void attached(ISceneController sceneController);
    void detached();

    Region getSceneRegion();
    Parent getRoot();
}
