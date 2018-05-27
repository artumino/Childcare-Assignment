package com.polimi.childcare.client.ui.controllers.stages.anagrafica;

import com.polimi.childcare.client.ui.constants.ToolbarButtons;
import com.polimi.childcare.client.ui.controllers.ChildcareBaseStageController;
import com.polimi.childcare.client.ui.controllers.ISceneController;
import com.polimi.childcare.client.ui.controllers.ISubSceneController;
import com.polimi.childcare.client.ui.controls.DragAndDropTableView;
import com.polimi.childcare.client.ui.controls.LabelTextViewComponent;
import com.polimi.childcare.shared.entities.Contatto;
import com.polimi.childcare.shared.entities.Genitore;
import com.polimi.childcare.shared.entities.Pediatra;
import com.polimi.childcare.shared.entities.Persona;
import com.polimi.childcare.shared.utils.EntitiesHelper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

public class EditPersona implements ISubSceneController
{
    public static final String FXML_PATH = "fxml/stages/anagrafica/EditPersona.fxml";

    private Persona linkedPersona;
    private ChildcareBaseStageController stageController;
    private Parent root;

    @FXML private AnchorPane rootPane;

    @FXML private Tab tabDettagli;
    @FXML private Tab tabGenitori;
    @FXML private Tab tabContatti;
    @FXML private Tab tabTelefoni;

    @FXML private LabelTextViewComponent txtNome;
    @FXML private LabelTextViewComponent txtCognome;
    @FXML private LabelTextViewComponent txtCodiceFiscale;
    @FXML private DatePicker dpDataNascita;
    @FXML private LabelTextViewComponent txtStato;
    @FXML private LabelTextViewComponent txtCittadinanza;
    @FXML private LabelTextViewComponent txtComune;
    @FXML private LabelTextViewComponent txtProvincia;
    @FXML private LabelTextViewComponent txtResidenza;
    @FXML private ComboBox<String>  cbSesso;

    @FXML private DragAndDropTableView<Contatto> tableContatti;
    @FXML private DragAndDropTableView<Pediatra> tablePediatra;

    @FXML private DragAndDropTableView<Genitore> tableGenitori;

    @FXML private ListView<String> listTelefoni;
    @FXML private Button btnResetNumeri;

    @FXML private Button btnSalva;
    @FXML private Button btnElimina;

    @Override
    public void attached(ISceneController sceneController, Object... args)
    {
        if(sceneController instanceof ChildcareBaseStageController)
            this.stageController = (ChildcareBaseStageController)sceneController;


        if(this.stageController != null)
        {
            this.stageController.setToolbarButtonsVisibilityMask((byte)(ToolbarButtons.Close | ToolbarButtons.Minimize));

            if(args != null && args.length > 0 && args[0] instanceof Persona)
                this.linkedPersona = (Persona)args[0];

            this.stageController.requestSetTitle("Aggiorna - " + linkedPersona.getNome() + " " + linkedPersona.getCognome());

            TableColumn<Genitore, String> cName = new TableColumn<>("Nome");
            cName.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getNome()));

            //Nascondo l'header della tabella del pediatra
            /*tablePediatra.skinProperty().addListener((a, b, newSkin) -> {
                TableHeaderRow headerRow = ((TableViewSkinBase) newSkin).getTableHeaderRow();
                headerRow.setVisible(false);
                headerRow.setMaxHeight(0);
                headerRow.setMinHeight(0);
                headerRow.setPrefHeight(0);
            });*/
            TableColumn<Pediatra, String> cNomeContatto = new TableColumn<>("Nome");
            TableColumn<Pediatra, String> cCognomeContatto = new TableColumn<>("Congome");
            TableColumn<Pediatra, String> cDescrizioneContatto = new TableColumn<>("Descrizione");
            TableColumn<Pediatra, String> cTelefoniContatto = new TableColumn<>("Telefoni");
            cNomeContatto.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getNome()));
            cCognomeContatto.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getCognome()));
            cDescrizioneContatto.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getDescrizione()));
            cTelefoniContatto.setCellValueFactory(c -> new ReadOnlyStringWrapper(EntitiesHelper.getTelefoniStringFromIterable(c.getValue().getTelefoni())));

            tablePediatra.getColumns().addAll(cNomeContatto, cCognomeContatto, cDescrizioneContatto, cTelefoniContatto);
            //tableContatti.getColumns().addAll(cNomeContatto, cCognomeContatto, cDescrizioneContatto, cTelefoniContatto);

            //Tabella da un solo elemento
            tablePediatra.itemsProperty().addListener(((observable, oldValue, newValue) -> {
                if(oldValue.size() > 0)
                {
                    newValue.remove(oldValue.get(0));
                    tablePediatra.getItems().clear();
                }
            }));
            tablePediatra.dragForClass(Pediatra.class);

            tableGenitori.getColumns().addAll(cName);
            tableGenitori.dragForClass(Genitore.class);
        }
    }

    @Override
    public void detached()
    {

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
