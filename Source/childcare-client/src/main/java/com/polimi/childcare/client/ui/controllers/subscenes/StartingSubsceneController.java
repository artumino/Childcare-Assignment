package com.polimi.childcare.client.ui.controllers.subscenes;

import com.polimi.childcare.client.shared.networking.ClientNetworkManager;
import com.polimi.childcare.client.shared.networking.IClientNetworkInterface;
import com.polimi.childcare.client.networking.rmi.RMIInterfaceClient;
import com.polimi.childcare.client.shared.networking.sockets.SocketInterfaceClient;
import com.polimi.childcare.client.ui.constants.ToolbarButtons;
import com.polimi.childcare.client.ui.controllers.ChildcareBaseStageController;
import com.polimi.childcare.client.ui.controllers.ISceneController;
import com.polimi.childcare.client.ui.controllers.ISubSceneController;
import com.polimi.childcare.client.ui.controllers.MainMenuStageController;
import com.polimi.childcare.client.ui.controls.DragAndDropTableView;
import com.polimi.childcare.client.ui.controls.LabelRecurrenceComponent;
import com.polimi.childcare.client.ui.utils.StageUtils;
import com.polimi.childcare.shared.entities.Fornitore;
import com.polimi.childcare.shared.entities.Menu;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;

public class StartingSubsceneController implements ISubSceneController
{
    public static final String FXML_PATH = "fxml/StartingBox.fxml";

    @FXML private StackPane paneRoot;
    @FXML private Button btnConnect;
    @FXML private ComboBox<IClientNetworkInterface> cmbConnectionType;
    @FXML private VBox vboxContent;

    private ChildcareBaseStageController linkedStageController;
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

            vboxContent.getChildren().remove(lblConnectionError);

            boolean result;

            //Prova a connettersi con l'interfaccia
            if(cmbConnectionType.getValue() instanceof SocketInterfaceClient)
               result = ClientNetworkManager.getInstance().tryConnect("localhost", 55403);
            else
               result = ClientNetworkManager.getInstance().tryConnect("localhost", 55404);

            if(result)
            {
                try
                {
                    MainMenuStageController mainMenu = new MainMenuStageController();
                    mainMenu.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(this.linkedStageController != null)
                    this.linkedStageController.requestClose();
            }
            else
            {
                //Fallita connessione
                if(!vboxContent.getChildren().contains(lblConnectionError))
                    vboxContent.getChildren().add(lblConnectionError);
            }
        });
    }

    @Override
    public void attached(ISceneController sceneController, Object... args) {
        if(sceneController instanceof ChildcareBaseStageController)
        {
            this.linkedStageController = (ChildcareBaseStageController) sceneController;
            this.linkedStageController.requestSetTitle(""); //Niente titolo
            this.linkedStageController.setToolbarButtonsVisibilityMask((byte) (ToolbarButtons.Close | ToolbarButtons.Minimize));
        }
    }

    @Override
    public void detached() {
        //DO NOTHING...
    }

    @Override
    public Region getSceneRegion() {
        return paneRoot;
    }

    @Override
    public Parent getRoot() {
        return paneRoot;
    }

    @Override
    public Scene setupScene(Parent parent) {
        return paneRoot.getScene();
    }
}
