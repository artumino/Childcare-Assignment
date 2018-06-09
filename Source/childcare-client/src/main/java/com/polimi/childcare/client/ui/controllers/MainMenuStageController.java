package com.polimi.childcare.client.ui.controllers;

import com.polimi.childcare.client.ui.controllers.subscenes.AnagraficaSubsceneController;
import com.polimi.childcare.client.ui.controllers.subscenes.GitaSubsceneController;
import com.polimi.childcare.client.ui.controllers.subscenes.HomeSubsceneController;
import com.polimi.childcare.client.ui.controllers.subscenes.MensaSubsceneController;
import com.polimi.childcare.client.ui.utils.EffectsUtils;
import com.polimi.childcare.client.ui.utils.SceneUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
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

    @FXML private Label lblTitle;

    @FXML private Pane contentPane;

    public MainMenuStageController() throws IOException
    {
        this(MainMenuStageController.class.getClassLoader().getResource("fxml/MainStage.fxml"));
    }

    private MainMenuStageController(URL fxmlPath) throws IOException
    {
        super(fxmlPath);
    }

    @Override protected Node getWindowDragParent() {
        return this.dragToolbar;
    }
    @Override protected Node getMinimizeButton() { return this.btnMinimize; }
    @Override protected Node getMaximizeButton() { return this.btnMaximize; }
    @Override protected Node getCloseButton() { return this.btnClose; }
    @Override protected Pane getToolbarButtonsPane() { return null; }
    @Override protected Node getRootNode() { return this.rootStackPane; }
    @Override protected Label getTitleLabel() { return lblTitle; }

    //Mappa usata per associare ad ogni bottone una ed una sola scene istanziata all'apertura dello stage
    private HashMap<Node,ISubSceneController> menuItemsMap;
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
            menuItemsMap.put(btnHome, SceneUtils.loadSubScene(getClass().getClassLoader().getResource(HomeSubsceneController.FXML_PATH)));
            menuItemsMap.put(btnAnagrafica, SceneUtils.loadSubScene(getClass().getClassLoader().getResource(AnagraficaSubsceneController.FXML_PATH)));
            menuItemsMap.put(btnGite, SceneUtils.loadSubScene(getClass().getClassLoader().getResource(GitaSubsceneController.FXML_PATH)));
            menuItemsMap.put(btnMensa, SceneUtils.loadSubScene(getClass().getClassLoader().getResource(MensaSubsceneController.FXML_PATH)));
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

    @Override
    public void close() {
        super.close();

        //Force close application
        Platform.exit();
    }

    private void SelectMenuItem(Node item)
    {
        if(this.contentPane != null && menuItemsMap.containsKey(item))
        {
            //Rimuove tutti i bambini
            this.contentPane.getChildren().clear();

            if(this.currentSelectedMenuItem != null)
            {
                menuItemsMap.get(item).detached();
                EffectsUtils.RemoveGlow(this.currentSelectedMenuItem);
            }

            //Imposta la scena corretta come contanuto
            this.contentPane.getChildren().add(menuItemsMap.get(item).getRoot());

            //Aggiunge l'effetto glow all'oggetto selezionato
            this.currentSelectedMenuItem = item;
            EffectsUtils.AddGlow(item, 2);
            menuItemsMap.get(item).attached(this);
        }
    }

    @Override
    public void requestSetTitle(String newTitle)
    {
        if(lblTitle != null)
        {
            if(newTitle == null)
                lblTitle.setText("ChildCare");
            else
                lblTitle.setText("ChildCare - " + newTitle);
        }
    }
}
