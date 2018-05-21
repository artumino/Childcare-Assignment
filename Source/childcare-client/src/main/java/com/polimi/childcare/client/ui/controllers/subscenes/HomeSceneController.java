package com.polimi.childcare.client.ui.controllers.subscenes;

import com.polimi.childcare.client.shared.networking.ClientNetworkManager;
import com.polimi.childcare.client.shared.networking.NetworkOperation;
import com.polimi.childcare.client.ui.controllers.ISceneController;
import com.polimi.childcare.client.ui.controllers.IStageController;
import com.polimi.childcare.client.ui.controllers.ISubSceneController;
import com.polimi.childcare.shared.entities.Persona;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredBambiniRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListBambiniResponse;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class HomeSceneController implements ISubSceneController
{
    private Parent root;

    @FXML private AnchorPane rootPane;
    @FXML private TableView<Persona> tableList;

    //Lista persone da visualizzare
    private ObservableList<Persona> listaPersone;

    @FXML
    protected void initialize()
    {
        listaPersone = FXCollections.observableArrayList();

        //Imposto la scena
        TableColumn<Persona, String> name = new TableColumn<>("Nome");
        TableColumn<Persona, String> surname = new TableColumn<>("Cognome");
        TableColumn<Persona, String> fiscalCode = new TableColumn<>("Codice Fiscale");
        TableColumn<Persona, String> dateOfBirth = new TableColumn<>("Data di Nascita");
        TableColumn<Persona, Integer> id = new TableColumn<>("Matricola");

        name.setCellValueFactory((cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getNome()));
        surname.setCellValueFactory((cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getCognome()));
        fiscalCode.setCellValueFactory((cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getCodiceFiscale()));

        dateOfBirth.setCellValueFactory((cellData) -> new ReadOnlyStringWrapper(
                DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDateTime.ofInstant(cellData.getValue().getDataNascita().toInstant(), ZoneId.systemDefault()))));

        id.setCellValueFactory((cellData) -> new ReadOnlyObjectWrapper<>(cellData.getValue().getID()));

        if(tableList != null) {
            tableList.getColumns().addAll(name, surname, fiscalCode, dateOfBirth, id);
            tableList.setItems(listaPersone);
            //tableList.setColumnResizePolicy(p -> true);
        }
    }

    @Override
    public void attached(ISceneController sceneController)
    {
        //Se sono stato collegato ad uno stage, imposto un titolo opportuno
        if(sceneController instanceof IStageController)
            ((IStageController)sceneController).requestSetTitle("Home");

        //Provo ad aggiornare i dati
        ClientNetworkManager.getInstance().submitOperation(new NetworkOperation(
                new FilteredBambiniRequest(0, 0, false, null, new HashMap<>()),
                this::OnBambiniResponseRecived,
                true));
    }

    private void OnBambiniResponseRecived(BaseResponse response)
    {
        if(!(response instanceof ListBambiniResponse))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Errore nell'aggiornare i dati: " + (response != null ? "Bad Request" : "Risposta Nulla"));
            alert.showAndWait();
            return;
        }

        ListBambiniResponse bambiniResponse = (ListBambiniResponse)response;

        //Aggiorno i valori
        listaPersone.clear();

        //Aggiungo tutti i bambini
        if(bambiniResponse.getPayload() != null)
        {
            listaPersone.addAll(bambiniResponse.getPayload());
            tableList.sort();
        }

        //tableList.refresh();

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
