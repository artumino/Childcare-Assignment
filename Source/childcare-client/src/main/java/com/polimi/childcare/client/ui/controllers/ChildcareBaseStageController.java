package com.polimi.childcare.client.ui.controllers;

import com.polimi.childcare.client.ui.utils.EffectsUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class ChildcareBaseStageController extends UndecoratedDraggableStageController
{

    @FXML private StackPane rootStackPane;
    @FXML private ToolBar dragToolbar;

    @FXML private ImageView btnClose;
    @FXML private ImageView btnMinimize;
    @FXML private ImageView btnMaximize;

    @FXML private Label lblTitle;

    @Override public String getTitle()
    {
        return "ChildCare";
    }
    @Override protected Node getWindowDragParent() {
        return this.dragToolbar;
    }
    @Override protected Node getMinimizeButton() { return this.btnMinimize; }
    @Override protected Node getMaximizeButton() { return this.btnMaximize; }
    @Override protected Node getCloseButton() { return this.btnClose; }
    @Override protected Node getRootNode() { return this.rootStackPane; }


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
    protected void initialize()
    {
        addGlowEffectEvents(btnClose);
        addGlowEffectEvents(btnMaximize);
        addGlowEffectEvents(btnMinimize);

        super.initialize();
    }

    private void addGlowEffectEvents(Node node)
    {
        if(node != null)
        {
            node.setOnMouseEntered(mouseEvent -> EffectsUtils.AddGlow(node, 2));
            node.setOnMouseExited(mouseEvent -> EffectsUtils.RemoveGlow(node));
        }
    }

    @Override
    public void requestSetTitle(String newTitle)
    {
        if(lblTitle != null)
            lblTitle.setText(newTitle);
    }
}
