package com.polimi.childcare.client.ui.controllers.subscenes;

import com.polimi.childcare.client.ui.controllers.BaseStageController;
import com.polimi.childcare.client.ui.controllers.ISceneController;
import com.polimi.childcare.client.ui.controllers.ISubSceneController;
import com.polimi.childcare.client.ui.controls.GruppoContainerComponent;
import com.polimi.childcare.client.ui.utils.EffectsUtils;
import com.polimi.childcare.shared.entities.Gruppo;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

import java.util.ArrayList;
import java.util.List;

public class GitaSubsceneController extends NetworkedSubScene implements ISubSceneController
{
    public static final String FXML_PATH = "fxml/GitaScene.fxml";

    private Parent root;

    @FXML private AnchorPane rootPane;
    @FXML private Button btnRefresh;

    //Gruppi
    @FXML private HBox hboxGruppi;
    @FXML private ImageView imgAddGroup;

    @FXML
    protected void initialize()
    {
        //RefreshDelleListe
        btnRefresh.setOnMouseClicked(event -> RefreshData());


        List<Gruppo> gruppoList = new ArrayList<>(5);
        for(int i = 0; i < 5; i++)
        {
            Gruppo gruppo = new Gruppo();
            gruppo.unsafeSetID(i);
            gruppoList.add(gruppo);
        }

        if(hboxGruppi != null)
        {
            hboxGruppi.getChildren().clear();
            for (Gruppo gruppo : gruppoList) {
                GruppoContainerComponent containerComponent = new GruppoContainerComponent(gruppo);
                containerComponent.setOnDeleteClicked(ignored -> {});
                containerComponent.setDragEnabled(true);
                hboxGruppi.getChildren().add(containerComponent);
            }

            if(imgAddGroup != null)
                hboxGruppi.getChildren().add(imgAddGroup);
        }

        //Effects
        if(imgAddGroup != null)
        {
            imgAddGroup.setOnMouseEntered(event -> EffectsUtils.AddShadow(imgAddGroup, 2));
            imgAddGroup.setOnMouseExited(event -> EffectsUtils.RemoveShadow(imgAddGroup));
        }
    }

    @Override
    public void attached(ISceneController sceneController, Object... args)
    {
        //Se sono stato collegato ad uno stage, imposto un titolo opportuno
        if(sceneController instanceof BaseStageController)
            ((BaseStageController)sceneController).requestSetTitle("Gita");

        RefreshData();
    }

    private void RefreshData()
    {
        //Provo ad aggiornare i dati
        /*networkOperationVault.submitOperation(new NetworkOperation(
                new FilteredBambiniRequest(0, 0, false),
                this::OnBambiniResponseRecived,
                true));

        networkOperationVault.submitOperation(new NetworkOperation(
                new FilteredLastPresenzaRequest(0, 0, false),
                this::OnPresenzeRecived,
                true));*/
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
