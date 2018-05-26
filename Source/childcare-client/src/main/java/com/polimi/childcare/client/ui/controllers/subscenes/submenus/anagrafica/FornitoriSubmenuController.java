package com.polimi.childcare.client.ui.controllers.subscenes.submenus.anagrafica;

import com.jfoenix.controls.JFXButton;
import com.polimi.childcare.client.shared.networking.ClientNetworkManager;
import com.polimi.childcare.client.shared.networking.NetworkOperation;
import com.polimi.childcare.client.ui.controllers.ISceneController;
import com.polimi.childcare.client.ui.filters.Filters;
import com.polimi.childcare.client.ui.utils.StageUtils;
import com.polimi.childcare.client.ui.utils.TableUtils;
import com.polimi.childcare.shared.entities.Fornitore;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredFornitoriRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListFornitoriResponse;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class FornitoriSubmenuController extends AnagraficaSubmenuBase<Fornitore>
{
    //Generated
    private TextField filterField;
    private Button btnUpdate;

    //Network
    private NetworkOperation pendingOperation;

    @Override
    protected Collection<TableColumn<Fornitore, ?>> setupColumns()
    {
        TableColumn<Fornitore, String> ragioneSociale = new TableColumn<>("Ragione Sociale");
        TableColumn<Fornitore, String> partitaIVA = new TableColumn<>("PartitaIVA");
        TableColumn<Fornitore, String> registroImprese = new TableColumn<>("Registro Imprese");
        TableColumn<Fornitore, String> sedeLegale = new TableColumn<>("Sede Legale");
        TableColumn<Fornitore, String> iban = new TableColumn<>("IBAN");
        TableColumn<Fornitore, String> telefoni = new TableColumn<>("Telefoni");
        TableColumn<Fornitore, String> fax = new TableColumn<>("Fax");

        ragioneSociale.setCellValueFactory((cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getRagioneSociale()));
        partitaIVA.setCellValueFactory((cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getPartitaIVA()));
        registroImprese.setCellValueFactory((cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getNumeroRegistroImprese()));
        sedeLegale.setCellValueFactory((cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getSedeLegale()));
        iban.setCellValueFactory((cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getIBAN()));
        telefoni.setCellValueFactory((cellData) -> new ReadOnlyStringWrapper(TableUtils.iterableToString(cellData.getValue().getTelefoni())));
        fax.setCellValueFactory((cellData) -> new ReadOnlyStringWrapper(TableUtils.iterableToString(cellData.getValue().getFax())));

        return Arrays.asList(ragioneSociale, partitaIVA, registroImprese, sedeLegale, iban ,telefoni ,fax);
    }

    @Override
    protected void setupFilterNodes()
    {
        //Crea filtri
        filterField = new TextField();
        filterField.setPromptText("Filtra...");
        filterComponent.addFilterField(filterField.textProperty(), (fornitore -> Filters.filterFornitore(fornitore, filterField.getText())));
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

    private void refreshData()
    {
        if(this.pendingOperation != null)
            ClientNetworkManager.getInstance().abortOperation(this.pendingOperation);

        this.pendingOperation = new NetworkOperation(
                new FilteredFornitoriRequest(0, 0, false, null, null),
                this::OnFornitoriResponseRecived,
                true);

        //Provo ad aggiornare i dati
        ClientNetworkManager.getInstance().submitOperation(this.pendingOperation);
    }

    private void OnFornitoriResponseRecived(BaseResponse response)
    {
        this.pendingOperation = null;

        if(!(response instanceof ListFornitoriResponse))
        {
            StageUtils.ShowAlert(Alert.AlertType.ERROR, "Immpossibile ricevere lista fornitori al momento");
            return;
        }

        ListFornitoriResponse fornitoriResponse = (ListFornitoriResponse)response;
        filteredList.updateDataSet(fornitoriResponse.getPayload());
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
