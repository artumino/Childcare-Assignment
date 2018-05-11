package com.polimi.childcare.client.ui.controllers;

import javafx.scene.Parent;
import javafx.stage.Stage;

public class MainMenuController extends BaseController
{
    private String title;

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MainMenuController()
    {
        this.title = "ChildCare Portal";
    }
}
