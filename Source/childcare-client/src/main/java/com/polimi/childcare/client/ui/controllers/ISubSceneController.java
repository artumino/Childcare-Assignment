package com.polimi.childcare.client.ui.controllers;

import javafx.scene.Parent;

public interface ISubSceneController extends ISceneController
{
    void attached(ISceneController sceneController);
    void detached();

    Parent getRoot();
}
