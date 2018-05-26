package com.polimi.childcare.client.ui.controllers;

import com.polimi.childcare.client.ui.utils.EffectsUtils;
import com.polimi.childcare.client.ui.utils.SceneUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
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

    private boolean locked;
    private ISubSceneController contentScene;

    public ChildcareBaseStageController() throws IOException
    {
        this(ChildcareBaseStageController.class.getClassLoader().getResource("fxml/ChildcareBaseStage.fxml"));
    }

    private ChildcareBaseStageController(URL fxmlPath) throws IOException
    {
        super(fxmlPath);
    }

    @Override protected Region getWindowDragParent() { return this.dragToolbar; }
    @Override protected Node getMinimizeButton() { return this.btnMinimize; }
    @Override protected Node getMaximizeButton() { return this.btnMaximize; }
    @Override protected Node getCloseButton() { return this.btnClose; }
    @Override protected Pane getToolbarButtonsPane() { return this.hboxToolbarButtons; }
    @Override protected Node getRootNode() { return this.rootStackPane; }
    @Override protected Label getTitleLabel() { return this.lblTitle; }

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
    }

    private void addGlowEffectEvents(Node node)
    {
        if(node != null)
        {
            node.setOnMouseEntered(mouseEvent -> EffectsUtils.AddGlow(node, 2));
            node.setOnMouseExited(mouseEvent -> EffectsUtils.RemoveGlow(node));
        }
    }

    //Gestione del contenuto
    public ISubSceneController getContentScene()
    {
        return contentScene;
    }

    public void setContentScene(URL contentScene, Object... args) {
        try {

            //Carica la nuova scene
            ISubSceneController newScene = SceneUtils.loadSubScene(contentScene);
            //Solo se c'è qualcosa, sennò aspetto l'initialize
            UpdateContentScene(newScene, args);
            this.contentScene = newScene;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void UpdateContentScene(ISubSceneController newScene, Object... args)
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
            this.rootStackPane.setPrefHeight(getWindowDragParent().getPrefHeight() + newScene.getSceneRegion().getPrefHeight());
            this.rootStackPane.setPrefWidth(newScene.getSceneRegion().getPrefWidth());
            this.setMinWidth(getControllerWidth());
            this.setMinHeight(getControllerHeight());
            setWidth(getControllerWidth());
            setHeight(getControllerHeight());
            this.contentPane.getChildren().add(newScene.getRoot());
            newScene.attached(this, args);
        }
    }

    @Override
    public void close()
    {
        if(!this.locked)
            super.close();
    }

    public void lock()
    {
        this.locked = true;

    }

    public void unlock()
    {
        this.locked = false;
    }
}
