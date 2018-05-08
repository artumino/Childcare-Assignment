package com.polimi.childcare.client.ui.controllers;

import com.jfoenix.controls.JFXButton;
import com.polimi.childcare.client.networking.ClientNetworkManager;
import com.polimi.childcare.client.networking.IClientNetworkInterface;
import com.polimi.childcare.client.networking.rmi.RMIInterfaceClient;
import com.polimi.childcare.client.networking.sockets.SocketInterfaceClient;
import com.sun.javafx.collections.ImmutableObservableList;
import com.sun.javafx.collections.ObservableListWrapper;
import com.sun.javafx.collections.ObservableMapWrapper;
import com.sun.javafx.collections.UnmodifiableObservableMap;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.SortedList;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class StartingBoxController extends StackPane
{
    private Stage linkedStage;
    @FXML private JFXButton btnConnect;
    @FXML private ComboBox<IClientNetworkInterface> cmbConnectionType;
    @FXML private FlowPane flowPane;

    private Label lblConnectionError;
    private ObservableList<IClientNetworkInterface> connectionTypes;

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

    public void setup(Stage stage, StackPane paneRoot)
    {
        Scene scene = new Scene(paneRoot, this.getPrefWidth(), this.getPrefHeight());
        this.linkedStage = stage;
        this.linkedStage.setTitle("Connect");
        this.linkedStage.setResizable(false);
        this.linkedStage.setScene(scene);
        //this.linkedStage.initStyle(StageStyle.UNDECORATED);
    }
}
