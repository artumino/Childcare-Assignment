package com.polimi.childcare.client.ui.controllers;

import com.polimi.childcare.client.ui.utils.EffectsUtils;
import com.polimi.childcare.client.ui.utils.SceneUtils;
import com.polimi.childcare.client.ui.utils.StageUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.HashMap;

public class MainMenuStageController extends UndecoratedDraggableStageController
{

    @FXML private StackPane rootStackPane;
    @FXML private ToolBar dragToolbar;

    @FXML private ImageView btnClose;
    @FXML private ImageView btnMinimize;
    @FXML private ImageView btnMaximize;

    @FXML private Node btnHome;
    @FXML private Node btnAnagrafica;
    @FXML private Node btnGite;
    @FXML private Node btnMensa;

    @FXML private Pane contentPane;

    @Override protected Node getWindowDragParent() {
        return this.dragToolbar;
    }
    @Override protected Node getMinimizeButton() { return this.btnMinimize; }
    @Override protected Node getMaximizeButton() { return this.btnMaximize; }
    @Override protected Node getCloseButton() { return this.btnClose; }
    @Override protected Node getRootNode() { return this.rootStackPane; }

    //Mappa usata per associare ad ogni bottone una ed una sola scene istanziata all'apertura dello stage
    private HashMap<Node,Parent> menuItemsMap;
    private Node currentSelectedMenuItem;

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
        //Crea Menu
        menuItemsMap = new HashMap<>(4);
        try {
            menuItemsMap.put(btnHome, SceneUtils.loadScene(getClass().getClassLoader().getResource("fxml/HomeScene.fxml")));
            menuItemsMap.put(btnAnagrafica, SceneUtils.loadScene(getClass().getClassLoader().getResource("fxml/HomeScene.fxml")));
            menuItemsMap.put(btnGite, SceneUtils.loadScene(getClass().getClassLoader().getResource("fxml/HomeScene.fxml")));
            menuItemsMap.put(btnMensa, SceneUtils.loadScene(getClass().getClassLoader().getResource("fxml/HomeScene.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        btnHome.setOnMouseClicked((ev) -> SelectMenuItem(btnHome));
        btnAnagrafica.setOnMouseClicked((ev) -> SelectMenuItem(btnAnagrafica));
        btnGite.setOnMouseClicked((ev) -> SelectMenuItem(btnGite));
        btnMensa.setOnMouseClicked((ev) -> SelectMenuItem(btnMensa));

        //Seleziono elemento di default
        if(currentSelectedMenuItem == null)
            SelectMenuItem(btnHome);

        //Imposta altri eventi
        addGlowEffectEvents(btnClose);
        addGlowEffectEvents(btnMaximize);
        addGlowEffectEvents(btnMinimize);

        addGlowEffectEvents(btnHome);
        addGlowEffectEvents(btnAnagrafica);
        addGlowEffectEvents(btnGite);
        addGlowEffectEvents(btnMensa);

        super.initialize();
    }

    private void addGlowEffectEvents(Node node)
    {
        if(node != null)
        {
            node.setOnMouseEntered(mouseEvent -> EffectsUtils.AddGlow(node, 2));

            //Rimuove il Glow solo agli oggetti non attualmente selezionati
            node.setOnMouseExited(mouseEvent -> {
                if(currentSelectedMenuItem != node)
                    EffectsUtils.RemoveGlow(node);
            });
        }
    }

    private void SelectMenuItem(Node item)
    {
        if(this.contentPane != null && menuItemsMap.containsKey(item))
        {
            //TODO: Notifica detached
            //Rimuove tutti i bambini
            this.contentPane.getChildren().clear();

            if(this.currentSelectedMenuItem != null)
                EffectsUtils.RemoveGlow(this.currentSelectedMenuItem);

            //Imposta la scena corretta come contanuto
            this.contentPane.getChildren().add(menuItemsMap.get(item));

            //Aggiunge l'effetto glow all'oggetto selezionato
            this.currentSelectedMenuItem = item;
            EffectsUtils.AddGlow(item, 2);
            //TODO: Notifica Attached
        }
    }
}
