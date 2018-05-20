package com.polimi.childcare.client.ui.controllers.subscenes;

import com.jfoenix.controls.JFXButton;
import com.polimi.childcare.client.ui.controllers.ISceneController;
import com.polimi.childcare.client.ui.controllers.IStageController;
import com.polimi.childcare.client.ui.controllers.ISubSceneController;
import com.polimi.childcare.client.ui.controllers.subscenes.submenus.anagrafica.PersoneSubmenuController;
import com.polimi.childcare.client.ui.utils.SceneUtils;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.util.HashMap;

public class AnagraficaSceneController implements ISubSceneController
{
    private Parent root;

    @FXML private AnchorPane rootPane;
    @FXML private Pane paneContent;
    @FXML private ToolBar toolBarTopMenu;

    //Submenu handling
    private HashMap<JFXButton,ISubSceneController> subMenuItemsMap;
    private JFXButton selectedSubmenu;

    @FXML
    protected void initialize()
    {
        //Crea Menu
        subMenuItemsMap = new HashMap<>(4);

        JFXButton btnPersone = new JFXButton("Persone");
        btnPersone.getStyleClass().add("submenu-button");
        btnPersone.setOnMouseClicked((event -> selectSubmenu(btnPersone)));


        JFXButton btnFornitori = new JFXButton("Fornitori");
        btnFornitori.getStyleClass().add("submenu-button");
        btnFornitori.setOnMouseClicked((event -> selectSubmenu(btnFornitori)));


        JFXButton btnContatti = new JFXButton("Contatti");
        btnContatti.getStyleClass().add("submenu-button");
        btnContatti.setOnMouseClicked((event -> selectSubmenu(btnContatti)));


        JFXButton btnPediatri = new JFXButton("Pediatri");
        btnPediatri.getStyleClass().add("submenu-button");
        btnPediatri.setOnMouseClicked((event -> selectSubmenu(btnPediatri)));

        try {
            subMenuItemsMap.put(btnPersone, SceneUtils.loadSubSceneWithController(getClass().getClassLoader().getResource("fxml/AnagraficaSubMenuBase.fxml"), PersoneSubmenuController.class));
            subMenuItemsMap.put(btnFornitori, SceneUtils.loadSubSceneWithController(getClass().getClassLoader().getResource("fxml/AnagraficaSubMenuBase.fxml"), PersoneSubmenuController.class));
            subMenuItemsMap.put(btnContatti, SceneUtils.loadSubSceneWithController(getClass().getClassLoader().getResource("fxml/AnagraficaSubMenuBase.fxml"), PersoneSubmenuController.class));
            subMenuItemsMap.put(btnPediatri, SceneUtils.loadSubSceneWithController(getClass().getClassLoader().getResource("fxml/AnagraficaSubMenuBase.fxml"), PersoneSubmenuController.class));
        } catch (IOException e) {
            e.printStackTrace();
        }

        btnPersone.setOnMouseClicked((ev) -> selectSubmenu(btnPersone));
        btnFornitori.setOnMouseClicked((ev) -> selectSubmenu(btnFornitori));
        btnContatti.setOnMouseClicked((ev) -> selectSubmenu(btnContatti));
        btnPediatri.setOnMouseClicked((ev) -> selectSubmenu(btnPediatri));


        if(this.toolBarTopMenu != null)
        {
            //this.toolBarTopMenu.getItems().addAll(subMenuItemsMap.keySet()); //Non è ordinato, è un set
            this.toolBarTopMenu.getItems().addAll(btnPersone, btnFornitori, btnContatti, btnPediatri); //Ordinato
            if (this.selectedSubmenu == null)
                selectSubmenu(btnPersone);
        }
    }

    private void selectSubmenu(JFXButton selectedSubmenu)
    {
        if(selectedSubmenu != null && paneContent != null && subMenuItemsMap.containsKey(selectedSubmenu))
        {
            if(this.selectedSubmenu != null)
            {
                this.selectedSubmenu.getStyleClass().remove("submenu-button-active");
                subMenuItemsMap.get(this.selectedSubmenu).detached();
            }

            this.paneContent.getChildren().clear();

            this.selectedSubmenu = selectedSubmenu;
            this.selectedSubmenu.getStyleClass().add("submenu-button-active");

            this.paneContent.getChildren().add(subMenuItemsMap.get(selectedSubmenu).getRoot());
            subMenuItemsMap.get(this.selectedSubmenu).attached(this);
        }
    }

    @Override
    public void attached(ISceneController sceneController)
    {
        //Se sono stato collegato ad uno stage, imposto un titolo opportuno
        if(sceneController instanceof IStageController)
            ((IStageController)sceneController).requestSetTitle("Home");
    }

    @Override
    public void detached()
    {
        //DO NOTHING...
    }

    @Override
    public Region getSceneRegion() {
        return this.rootPane;
    }

    @Override
    public Parent getRoot()
    {
        return this.root;
    }

    @Override
    public Scene setupScene(Parent parent)
    {
        this.root = parent;
        return root.getScene();
    }
}
