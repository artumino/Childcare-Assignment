package com.polimi.childcare.client.ui.controllers.stages.generic;

import com.polimi.childcare.client.shared.networking.NetworkOperation;
import com.polimi.childcare.client.ui.OrderedFilteredList;
import com.polimi.childcare.client.ui.components.FilterComponent;
import com.polimi.childcare.client.ui.constants.ToolbarButtons;
import com.polimi.childcare.client.ui.controllers.ChildcareBaseStageController;
import com.polimi.childcare.client.ui.controllers.ISceneController;
import com.polimi.childcare.client.ui.controllers.ISubSceneController;
import com.polimi.childcare.client.ui.controllers.subscenes.NetworkedSubScene;
import com.polimi.childcare.client.ui.controls.DragAndDropTableView;
import com.polimi.childcare.client.ui.utils.StageUtils;
import com.polimi.childcare.shared.entities.Persona;
import com.polimi.childcare.shared.entities.ReazioneAvversa;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredReazioneAvversaRequest;
import com.polimi.childcare.shared.networking.responses.lists.ListReazioneAvversaResponse;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

public class ReazioniAvverseStage extends NetworkedSubScene implements ISubSceneController
{
    public static final String FXML_PATH = "fxml/stages/generic/ReazioniAvverseStage.fxml";

    private ChildcareBaseStageController stageController;
    private Parent root;

    private OrderedFilteredList<ReazioneAvversa> listReazioniAvverse;
    private FilterComponent<ReazioneAvversa> filterReazioniAvverse;

    @FXML private AnchorPane rootPane;

    @FXML private TextField txtFiltro;
    @FXML private DragAndDropTableView<ReazioneAvversa> tableReazioniAvverse;


    @Override
    public void attached(ISceneController sceneController, Object... args)
    {
        if(sceneController instanceof ChildcareBaseStageController)
            this.stageController = (ChildcareBaseStageController)sceneController;


        if(this.stageController != null)
        {
            this.stageController.requestSetTitle("Reazioni Avverse");
            this.listReazioniAvverse = new OrderedFilteredList<>();
            this.filterReazioniAvverse = new FilterComponent<>(this.listReazioniAvverse.predicateProperty());
            this.stageController.setToolbarButtonsVisibilityMask(ToolbarButtons.Close);

            networkOperationVault.submitOperation(new NetworkOperation(new FilteredReazioneAvversaRequest(0,0, false),
            respose ->
            {
                if(!(respose instanceof ListReazioneAvversaResponse))
                {
                    StageUtils.ShowAlert(Alert.AlertType.ERROR, "Impossibile ricevere le reazioni avverse dal server!");
                    stageController.requestClose();
                }
                else
                    listReazioniAvverse.updateDataSet(((ListReazioneAvversaResponse)respose).getPayload());
            }, true));

            //Setup Tabella
            TableColumn<ReazioneAvversa, String> cNome = new TableColumn<>("Nome");
            TableColumn<ReazioneAvversa, String> cDescrizione = new TableColumn<>("Descrizione");
            cNome.setMinWidth(100);
            cNome.setPrefWidth(150);
            cNome.setMaxWidth(200);
            cNome.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getNome()));
            cDescrizione.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getDescrizione()));

            //Imposto l'a capo automatico
            cDescrizione.setCellFactory(tc -> {
                TableCell<ReazioneAvversa, String> cell = new TableCell<>();
                Text text = new Text();
                cell.setGraphic(text);
                cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
                text.wrappingWidthProperty().bind(cDescrizione.widthProperty());
                text.textProperty().bind(cell.itemProperty());
                return cell ;
            });

            if(txtFiltro != null)
            {
                this.filterReazioniAvverse.addFilterField(txtFiltro.textProperty(), p ->
                {
                    String queryString = txtFiltro.getText().toLowerCase().trim();
                    return p.getNome().toLowerCase().trim().contains(queryString) || p.getDescrizione().toLowerCase().trim().contains(queryString);
                });
            }

            if(tableReazioniAvverse != null)
            {
                tableReazioniAvverse.getColumns().addAll(cNome, cDescrizione);
                listReazioniAvverse.comparatorProperty().bind(tableReazioniAvverse.comparatorProperty());
                tableReazioniAvverse.setItems(listReazioniAvverse.list());
            }
        }
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
