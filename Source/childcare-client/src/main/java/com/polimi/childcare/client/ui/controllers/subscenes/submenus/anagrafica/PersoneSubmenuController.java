package com.polimi.childcare.client.ui.controllers.subscenes.submenus.anagrafica;

import com.polimi.childcare.client.shared.networking.ClientNetworkManager;
import com.polimi.childcare.client.shared.networking.NetworkOperation;
import com.polimi.childcare.client.ui.controllers.ISceneController;
import com.polimi.childcare.client.ui.utils.DateUtils;
import com.polimi.childcare.shared.entities.Persona;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredPersonaRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListPersoneResponse;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

import java.util.HashMap;

public class PersoneSubmenuController extends AnagraficaSubmenuBase<Persona>
{

    //Lista persone da visualizzare
    private ObservableList<Persona> listaPersone;
    private FilteredList<Persona> filteredPersone;
    private SortedList<Persona> sortedPersone;

    //Generated
    private TextField filterField;

    //Network
    private NetworkOperation pendingOperation;

    @Override
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
                DateUtils.dateToShortString(cellData.getValue().getDataNascita())));

        id.setCellValueFactory((cellData) -> new ReadOnlyObjectWrapper<>(cellData.getValue().getID()));

        //Crea Lista filtrata ed imposto filtro
        filteredPersone = new FilteredList<>(listaPersone, p -> true);

        //Crea lista ordinata
        sortedPersone = new SortedList<>(filteredPersone);

        if(tableView != null) {
            tableView.getColumns().addAll(name, surname, fiscalCode, dateOfBirth, id);
            sortedPersone.comparatorProperty().bind(tableView.comparatorProperty());
            tableView.setItems(sortedPersone);
            //tableList.setColumnResizePolicy(p -> true);
        }

        //Crea filtri
        filterField = new TextField();
        filterField.setPromptText("Filtra...");
        filterField.textProperty().addListener((observable, oldValue, newValue) ->
        {
            //Se diverso filtra, sennò accetta tutti i valori
            if(newValue != null && !newValue.isEmpty() && !oldValue.equals(newValue))
                filterList();
            else
                filteredPersone.setPredicate(p -> true);
        });
        vboxFilters.getChildren().add(filterField);
    }

    private void filterList()
    {
        if(filteredPersone != null)
        {
            filteredPersone.setPredicate(persona -> {
                String lowerCaseFilter = filterField.getText().toLowerCase();

                try
                {
                    //Se è una matricola
                    Integer.parseInt(lowerCaseFilter);
                    return String.valueOf(persona.getID()).contains(lowerCaseFilter);
                }
                catch (Exception ex)
                {
                    //Se è qualcos'altro
                    if(persona.getNome().toLowerCase().contains(lowerCaseFilter))
                        return true;
                    if(persona.getCognome().toLowerCase().contains(lowerCaseFilter))
                        return true;
                    if(DateUtils.dateToShortString(persona.getDataNascita()).contains(lowerCaseFilter))
                        return true;
                    return persona.getCodiceFiscale().toLowerCase().contains(lowerCaseFilter);
                }
            });
        }
    }

    @Override
    public void attached(ISceneController sceneController)
    {
        if(this.pendingOperation != null)
            ClientNetworkManager.getInstance().abortOperation(this.pendingOperation);

        this.pendingOperation = new NetworkOperation(
                new FilteredPersonaRequest(0, 0, false, null, new HashMap<>()),
                this::OnPersoneResponseRecived,
                true);

        //Provo ad aggiornare i dati
        ClientNetworkManager.getInstance().submitOperation(this.pendingOperation);
    }

    private void OnPersoneResponseRecived(BaseResponse response)
    {
        this.pendingOperation = null;

        if(!(response instanceof ListPersoneResponse))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Errore nell'aggiornare i dati: " + (response != null ? "Bad Request" : "Risposta Nulla"));
            alert.showAndWait();
            return;
        }

        ListPersoneResponse bambiniResponse = (ListPersoneResponse)response;

        //Aggiorno i valori
        listaPersone.clear();

        //Aggiungo tutti i bambini
        if(bambiniResponse.getPayload() != null)
        {
            listaPersone.addAll(bambiniResponse.getPayload());
            tableView.sort();
        }
    }

    @Override
    public void detached()
    {
        if(this.pendingOperation != null)
            ClientNetworkManager.getInstance().abortOperation(this.pendingOperation);
        this.pendingOperation = null;
    }
}
