package com.polimi.childcare.client.ui.controllers.subscenes.submenus.anagrafica;

import com.jfoenix.controls.JFXButton;
import com.polimi.childcare.client.shared.networking.ClientNetworkManager;
import com.polimi.childcare.client.shared.networking.NetworkOperation;
import com.polimi.childcare.client.ui.controllers.ChildcareBaseStageController;
import com.polimi.childcare.client.ui.controllers.ISceneController;
import com.polimi.childcare.client.ui.controllers.stages.anagrafica.EditContattoStageController;
import com.polimi.childcare.client.ui.filters.Filters;
import com.polimi.childcare.client.ui.utils.StageUtils;
import com.polimi.childcare.client.ui.utils.TableUtils;
import com.polimi.childcare.shared.entities.Contatto;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredContattoOnlyRequest;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredPediatraRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListContattoResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListPediatraResponse;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.*;

public class ContattiSubmenuController extends AnagraficaSubmenuBase<Contatto>
{
    //Generated
    private TextField filterField;
    private Button btnUpdate;
    protected Button btnAddContatto;

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

        tableView.setOnMousePressed(click -> {
            if(click.isPrimaryButtonDown() && click.getClickCount() == 2 && tableView.getSelectionModel().getSelectedItem() != null)
                ShowContattoDetails(tableView.getSelectionModel().getSelectedItem());
        });

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

        if(btnAddContatto == null)
        {
            btnAddContatto = new JFXButton("Aggiungi Contatto");
            btnAddContatto.setMaxWidth(Double.MAX_VALUE);
            btnAddContatto.setOnMousePressed(this::OnNewContattoClicked);
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
        return Arrays.asList(btnUpdate, btnAddContatto);
    }

    @Override
    public void attached(ISceneController sceneController, Object... args)
    {
        refreshData();
    }

    protected void refreshData()
    {
        //Provo ad aggiornare i dati
        networkOperationVault.submitOperation(new NetworkOperation(
                new FilteredContattoOnlyRequest(0, 0, false),
                this::OnContattiResponseRecived,
                true));
    }

    public void OnContattiResponseRecived(BaseResponse response)
    {
        networkOperationVault.operationDone(FilteredContattoOnlyRequest.class);
        networkOperationVault.operationDone(FilteredPediatraRequest.class);

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

    protected void ShowContattoDetails(Contatto contatto)
    {
        try {
            ChildcareBaseStageController editContattoController = new ChildcareBaseStageController();
            editContattoController.setContentScene(getClass().getClassLoader().getResource(EditContattoStageController.FXML_PATH), contatto);
            //setPresenzeStage.initOwner(getRoot().getScene().getWindow());
            editContattoController.setOnClosingCallback((returnArgs) -> {
                refreshData();
            });
            editContattoController.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void OnNewContattoClicked(MouseEvent ignored)
    {
        ShowContattoDetails(new Contatto());
    }
}
