package com.polimi.childcare.client.ui.controllers;

import com.jfoenix.controls.JFXButton;
import com.polimi.childcare.client.networking.ClientNetworkManager;
import com.polimi.childcare.client.networking.IClientNetworkInterface;
import com.polimi.childcare.client.networking.rmi.RMIInterfaceClient;
import com.polimi.childcare.client.networking.sockets.SocketInterfaceClient;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class StartingBoxController extends BaseController
{
    private Stage linkedStage;
    @FXML private StackPane paneRoot;
    @FXML private JFXButton btnConnect;
    @FXML private ComboBox<IClientNetworkInterface> cmbConnectionType;
    @FXML private FlowPane flowPane;

    private Label lblConnectionError;
    private ObservableList<IClientNetworkInterface> connectionTypes;



    @Override
    public String getTitle() {
        return "Connetti";
    }

    @Override
    //Ritorna la dimensione impostata da SceneBuilder oppure quella di default
    public double getControllerHeight() {
        return paneRoot != null ? paneRoot.getPrefHeight() : super.getControllerHeight();
    }

    @Override
    //Ritorna la dimensione impostata da SceneBuilder oppure quella di default
    public double getControllerWidth() {
        return paneRoot != null ? paneRoot.getPrefWidth() : super.getControllerHeight();
    }

    @FXML
    protected void initialize()
    {
        connectionTypes = new ObservableListWrapper<>(new ArrayList<>());
        connectionTypes.add(new SocketInterfaceClient());
        connectionTypes.add(new RMIInterfaceClient());

        lblConnectionError = new Label("Errore durante la connessione al server...");
        lblConnectionError.setStyle(lblConnectionError.getStyle() + "-fx-text-fill: #ff0000;");

        cmbConnectionType.setItems(new ObservableListWrapper<>(connectionTypes));
        cmbConnectionType.setValue(connectionTypes.get(0));

        btnConnect.setOnAction((action) -> {
            ClientNetworkManager.getInstance().SetInterface(cmbConnectionType.getValue());

            flowPane.getChildren().remove(lblConnectionError);

            try
            {
                //Prova a connettersi con l'interfaccia
                if(ClientNetworkManager.getInstance().GetCurrentInterface() instanceof SocketInterfaceClient)
                    ClientNetworkManager.getInstance().GetCurrentInterface().connect("localhost", 55403);
                else
                    ClientNetworkManager.getInstance().GetCurrentInterface().connect("localhost", 55404);

                this.linkedStage.close();


            } catch(IOException ex)
            {
                ex.printStackTrace();
                //Fallita connessione
                if(!flowPane.getChildren().contains(lblConnectionError))
                    flowPane.getChildren().add(lblConnectionError);
            }

        });
    }
}
