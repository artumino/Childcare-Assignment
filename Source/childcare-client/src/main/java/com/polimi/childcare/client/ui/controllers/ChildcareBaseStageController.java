package com.polimi.childcare.client.ui.controllers;

import com.polimi.childcare.client.ui.constants.ToolbarButtons;
import com.polimi.childcare.client.ui.utils.EffectsUtils;
import com.polimi.childcare.client.ui.utils.SceneUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;

public class ChildcareBaseStageController extends UndecoratedDraggableStageController
{
    @FXML private StackPane rootStackPane;
    @FXML private ToolBar dragToolbar;

    @FXML private HBox hboxToolbarButtons;
    @FXML private ImageView btnClose;
    @FXML private ImageView btnMinimize;
    @FXML private ImageView btnMaximize;

    @FXML private Label lblTitle;

    @FXML private Pane contentPane;

    private String title;
    private byte toolBarButtonVisibilityMask = (byte)0xff;
    private ISubSceneController contentScene;

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

        //Se la scena è stata impostata prima dell'initialize, aggiorno i parametri necessari
        if(this.contentScene != null)
            UpdateContentScene(this.contentScene);

        UpdateButtonVisibility();
    }

    private void addGlowEffectEvents(Node node)
    {
        if(node != null)
        {
            node.setOnMouseEntered(mouseEvent -> EffectsUtils.AddGlow(node, 2));
            node.setOnMouseExited(mouseEvent -> EffectsUtils.RemoveGlow(node));
        }
    }

    //Gestione del titolo
    @Override public String getTitle()
    {
        return this.title;
    }
    @Override
    public void requestSetTitle(String newTitle)
    {
        this.title = newTitle;
        if(lblTitle != null)
            lblTitle.setText(this.title);
        if(this.linkedStage != null)
            this.linkedStage.setTitle(this.title);
    }

    //Toolbar Buttons
    public void setToolbarButtonsVisibilityMask(byte newMask)
    {
        this.toolBarButtonVisibilityMask = newMask;
        UpdateButtonVisibility();
    }

    //Gestione del contenuto
    public ISubSceneController getContentScene()
    {
        return contentScene;
    }

    public void setContentScene(URL contentScene) {
        try {

            //Carica la nuova scene
            ISubSceneController newScene = SceneUtils.loadSubScene(contentScene);
            UpdateContentScene(newScene);
            this.contentScene = newScene;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void UpdateContentScene(ISubSceneController newScene)
    {

        //Notifica scene precedente che è stata rimossa dallo stage
        if(this.contentScene != null)
        {
            this.contentScene.detached();
            this.contentScene = null;
        }

        //Imposta la nuova scena
        if(this.contentPane != null && newScene != null && newScene.getRoot() != null)
        {
            this.contentPane.getChildren().clear();
            this.rootStackPane.setPrefHeight(getWindowDragParent().getLayoutBounds().getHeight() + newScene.getSceneRegion().getPrefHeight());
            this.rootStackPane.setPrefWidth(newScene.getSceneRegion().getPrefWidth());
            this.linkedStage.setWidth(getControllerWidth());
            this.linkedStage.setHeight(getControllerHeight());
            this.contentPane.getChildren().add(newScene.getRoot());
            newScene.attached(this);
        }
    }

    public boolean isButtonVisible(byte button)
    {
        return (this.toolBarButtonVisibilityMask & button) != 0;
    }

    private void UpdateButtonVisibility()
    {
        if(hboxToolbarButtons != null)
        {
            hboxToolbarButtons.getChildren().clear();

            if(isButtonVisible(ToolbarButtons.Minimize))
                hboxToolbarButtons.getChildren().add(btnMinimize);

            if(isButtonVisible(ToolbarButtons.Maximize))
                hboxToolbarButtons.getChildren().add(btnMaximize);

            if(isButtonVisible(ToolbarButtons.Close))
                hboxToolbarButtons.getChildren().add(btnClose);
        }
    }
}
