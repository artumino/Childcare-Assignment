package com.polimi.childcare.client.ui.controllers;

import com.polimi.childcare.client.ui.utils.EffectsUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ToolBar;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;

import javafx.scene.image.ImageView;

public class MainMenuStageController extends UndecoratedDraggableStageController
{

    @FXML private StackPane rootStackPane;
    @FXML private ToolBar dragToolbar;

    @FXML private ImageView btnClose;
    @FXML private ImageView btnMinimize;
    @FXML private ImageView btnMaximize;

    @Override
    public String getTitle()
    {
        return "ChildCare Portal";
    }


    @Override
    //Ritorna la dimensione impostata da SceneBuilder oppure quella di default
    public double getControllerHeight() {
        return rootStackPane != null ? rootStackPane.getPrefHeight() : super.getControllerHeight();
    }

    @Override
    //Ritorna la dimensione impostata da SceneBuilder oppure quella di default
    public double getControllerWidth() {
        return rootStackPane != null ? rootStackPane.getPrefWidth() : super.getControllerWidth();
    }

    @Override
    protected Node getWindowDragParent() {
        return this.dragToolbar;
    }

    @Override
    protected void initialize() {
        super.initialize();

        if(btnClose != null)
        {
            btnClose.setOnMouseEntered(mouseEvent -> EffectsUtils.AddGlow(btnClose, 2));
            btnClose.setOnMouseExited(mouseEvent -> EffectsUtils.RemoveGlow(btnClose));
        }

        if(btnMaximize != null)
        {
            btnMaximize.setOnMouseEntered(mouseEvent -> EffectsUtils.AddGlow(btnMaximize, 2));
            btnMaximize.setOnMouseExited(mouseEvent -> EffectsUtils.RemoveGlow(btnMaximize));
        }

        if(btnMinimize != null)
        {
            btnMinimize.setOnMouseEntered(mouseEvent -> EffectsUtils.AddGlow(btnMinimize, 2));
            btnMinimize.setOnMouseExited(mouseEvent -> EffectsUtils.RemoveGlow(btnMinimize));
        }
    }
}
