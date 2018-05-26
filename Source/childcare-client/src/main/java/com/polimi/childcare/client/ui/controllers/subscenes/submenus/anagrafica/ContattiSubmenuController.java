package com.polimi.childcare.client.ui.controllers.subscenes.submenus.anagrafica;

import com.jfoenix.controls.JFXButton;
import com.polimi.childcare.client.shared.networking.ClientNetworkManager;
import com.polimi.childcare.client.shared.networking.NetworkOperation;
import com.polimi.childcare.client.ui.controllers.ISceneController;
import com.polimi.childcare.client.ui.filters.Filters;
import com.polimi.childcare.client.ui.utils.StageUtils;
import com.polimi.childcare.client.ui.utils.TableUtils;
import com.polimi.childcare.shared.entities.Contatto;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredContattoOnlyRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListContattoResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListPediatraResponse;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

import java.util.*;

public class ContattiSubmenuController extends AnagraficaSubmenuBase<Contatto>
{
    //Generated
    private TextField filterField;
    private Button btnUpdate;

    //Network
    protected NetworkOperation pendingOperation;

    @Override
    protected Collection<TableColumn<Contatto, ?>> setupColumns()
    {
        TableColumn<Contatto, String> nome = new TableColumn<>("Nome");
        TableColumn<Contatto, String> cognome = new TableColumn<>("Cognome");
        TableColumn<Contatto, String> descrizione = new TableColumn<>("Descrizione");
        TableColumn<Contatto, String> indirizzo = new TableColumn<>("Indirizzo");
        TableColumn<Contatto, String> telefoni = new TableColumn<>("Numero di Telefono");

        nome.setCellValueFactory((cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getNome()));
        cognome.setCellValueFactory((cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getCognome()));
        descrizione.setCellValueFactory((cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getDescrizione()));
        indirizzo.setCellValueFactory((cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getIndirizzo()));
        telefoni.setCellValueFactory((cellData) -> new ReadOnlyStringWrapper(TableUtils.iterableToString(cellData.getValue().getTelefoni())));

        return Arrays.asList(nome, cognome, descrizione, indirizzo, telefoni);
    }

    @Override
    protected void setupFilterNodes()
    {
        //Crea filtri
        filterField = new TextField();
        filterField.setPromptText("Filtra...");
        filterComponent.addFilterField(filterField.textProperty(), (contatto -> Filters.filterContatto(contatto, filterField.getText())));
    }

    @Override
    protected void setupControlNodes()
    {
        if(btnUpdate == null)
        {
            btnUpdate = new JFXButton("Aggiorna");
            btnUpdate.setMaxWidth(Double.MAX_VALUE);
            btnUpdate.setOnMousePressed(event -> refreshData());
        }
    }

    @Override
    protected Collection<Node> getShownFilterElements()
    {
        return Collections.singletonList(filterField);
    }

    @Override
    protected Collection<Node> getShownControlElements()
    {
        return Collections.singletonList(btnUpdate);
    }

    @Override
    public void attached(ISceneController sceneController, Object... args)
    {
        refreshData();
    }

    protected void refreshData()
    {
        if(this.pendingOperation != null)
            ClientNetworkManager.getInstance().abortOperation(this.pendingOperation);

        this.pendingOperation = new NetworkOperation(
                new FilteredContattoOnlyRequest(0, 0, false, null, null),
                this::OnContattiResponseRecived,
                true);

        //Provo ad aggiornare i dati
        ClientNetworkManager.getInstance().submitOperation(this.pendingOperation);
    }

    public void OnContattiResponseRecived(BaseResponse response)
    {
        this.pendingOperation = null;

        if(!(response instanceof ListContattoResponse) && !(response instanceof ListPediatraResponse))
        {
            StageUtils.ShowAlert(Alert.AlertType.ERROR, "Immpossibile ricevere lista contatti al momento");
            return;
        }

        List<Contatto> recivedContatti = null;
        if(response instanceof ListContattoResponse)
            recivedContatti = new ArrayList<>(((ListContattoResponse) response).getPayload());
        else
            recivedContatti = new ArrayList<>(((ListPediatraResponse) response).getPayload());
        filteredList.updateDataSet(recivedContatti);
        tableView.refresh();
    }

    @Override
    public void detached()
    {
        if(this.pendingOperation != null)
            ClientNetworkManager.getInstance().abortOperation(this.pendingOperation);
        this.pendingOperation = null;
    }
}
