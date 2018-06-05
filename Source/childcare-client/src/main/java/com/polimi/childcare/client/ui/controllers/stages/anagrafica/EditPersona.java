package com.polimi.childcare.client.ui.controllers.stages.anagrafica;

import com.polimi.childcare.client.shared.networking.ClientNetworkManager;
import com.polimi.childcare.client.shared.networking.NetworkOperation;
import com.polimi.childcare.client.ui.constants.ToolbarButtons;
import com.polimi.childcare.client.ui.controllers.ChildcareBaseStageController;
import com.polimi.childcare.client.ui.controllers.ISceneController;
import com.polimi.childcare.client.ui.controllers.ISubSceneController;
import com.polimi.childcare.client.ui.controls.DragAndDropTableView;
import com.polimi.childcare.client.ui.controls.LabelTextViewComponent;
import com.polimi.childcare.client.ui.utils.StageUtils;
import com.polimi.childcare.shared.entities.*;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredPersonaRequest;
import com.polimi.childcare.shared.networking.responses.lists.ListPersoneResponse;
import com.polimi.childcare.shared.utils.EntitiesHelper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.io.IOException;

public class EditPersona implements ISubSceneController
{
    public static final String FXML_PATH = "fxml/stages/anagrafica/EditPersona.fxml";

    private NetworkOperation pendingOperation;

    private Persona linkedPersona;
    private ChildcareBaseStageController stageController;
    private Parent root;

    @FXML private AnchorPane rootPane;
    @FXML private AnchorPane loadingLayout;

    @FXML private TabPane layoutTabPane;
    @FXML private Tab tabDettagli;
    @FXML private Tab tabGenitori;
    @FXML private Tab tabBambini;
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

    @FXML private DragAndDropTableView<Bambino> tableBambini;

    @FXML private DragAndDropTableView<Diagnosi> tableDiagnosi;

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


            if(pendingOperation == null && linkedPersona != null)
            {
                this.loadingLayout.setVisible(true);
                this.pendingOperation = new NetworkOperation(new FilteredPersonaRequest(this.linkedPersona.getID(), true),
                        response ->
                        {
                            if(!(response instanceof ListPersoneResponse) || ((ListPersoneResponse)response).getPayload().size() == 0)
                            {
                                StageUtils.ShowAlert(Alert.AlertType.ERROR, "Errore, risposta non corretta dal server");
                                stageController.requestClose();
                            }
                            else
                            {
                                this.linkedPersona = ((ListPersoneResponse)response).getPayload().get(0);
                                updateData();
                                this.loadingLayout.setVisible(false);
                            }
                        },
                        true);
                ClientNetworkManager.getInstance().submitOperation(this.pendingOperation);
            }

            //Tabelle Generali
            printPersonaDetails();
            setupPhoneList();
            setupDiagnosi();

            setupTableBambini();

            //Tabelle per bambini
            setupTablePediatra();
            setupContatti();
            setupTableGenitori();

            //Aggiorna l'interfaccia con i presenti in linkedPersona
            updateData();
        }
    }

    private void updateData()
    {
        //Diagnosi
        if(linkedPersona != null && tableDiagnosi != null && linkedPersona.getDiagnosi() != null)
        {
            tableDiagnosi.getItems().clear();
            tableDiagnosi.getItems().addAll(linkedPersona.getDiagnosi());
        }

        //Telefoni
        if(this.linkedPersona != null && listTelefoni != null && this.linkedPersona.getTelefoni() != null)
        {
            listTelefoni.getItems().clear();
            listTelefoni.getItems().addAll(this.linkedPersona.getTelefoni());
        }

        //Bambini
        if((this.linkedPersona instanceof Genitore) && tableBambini != null && ((Genitore) this.linkedPersona).getBambini() != null)
        {
            tableBambini.getItems().clear();
            tableBambini.getItems().addAll(((Genitore) linkedPersona).getBambini());
        }

        //Genitori
        if((this.linkedPersona instanceof Bambino) && tableGenitori != null && ((Bambino) this.linkedPersona).getGenitori() != null)
        {
            tableGenitori.getItems().clear();
            tableGenitori.getItems().addAll(((Bambino) linkedPersona).getGenitori());
        }

        //Pediatra
        if((this.linkedPersona instanceof Bambino) && tablePediatra != null && ((Bambino) this.linkedPersona).getPediatra() != null)
        {
            tablePediatra.getItems().clear();
            tablePediatra.getItems().add(((Bambino)linkedPersona).getPediatra());
        }

        //Contatti
        if((this.linkedPersona instanceof Bambino) && ((Bambino) this.linkedPersona).getContatti() != null)
        {
            tableContatti.getItems().clear();
            tableContatti.getItems().addAll(((Bambino) linkedPersona).getContatti());
        }

        updateLayout();
    }

    private void setupDiagnosi()
    {
        TableColumn<Diagnosi, String> cNome = new TableColumn<>("Nome");
        TableColumn<Diagnosi, String> cDescrizione = new TableColumn<>("Descrizione");
        TableColumn<Diagnosi, Boolean> cAllergia= new TableColumn<>("Allergia");
        cNome.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getReazioneAvversa().getNome()));
        cDescrizione.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getReazioneAvversa().getDescrizione()));
        //cDescrizione.setMaxWidth(300);
        cAllergia.setCellFactory((cellData) -> new TableCell<Diagnosi, Boolean>()
        {
            @Override
            protected void updateItem(Boolean item, boolean empty)
            {
                if(!empty)
                {
                    Diagnosi diagnosi = (Diagnosi) getTableRow().getItem();
                    CheckBox checkBox = new CheckBox();
                    checkBox.setSelected(diagnosi.isAllergia());
                    checkBox.setOnMouseClicked(mouse -> diagnosi.setAllergia(checkBox.isSelected()));
                    setGraphic(checkBox);
                }
                else
                    setGraphic(null);
            }
        });

        tableDiagnosi.getColumns().addAll(cNome, cDescrizione, cAllergia);


        tableDiagnosi.dragForClass(Diagnosi.class);
    }

    private void setupPhoneList()
    {
        listTelefoni.setCellFactory(TextFieldListCell.forListView());

        listTelefoni.setOnEditCommit(t -> {
            if(!t.getNewValue().isEmpty())
                listTelefoni.getItems().set(t.getIndex(), t.getNewValue());
            else
                listTelefoni.getItems().remove(t.getIndex());
        });

        listTelefoni.setOnMousePressed(click -> {
            if(click.isPrimaryButtonDown() && click.getClickCount() == 2)
            {
                if(listTelefoni.getSelectionModel().getSelectedItem() == null)
                    listTelefoni.getItems().add("+39");
            }
        });

        btnResetNumeri.setOnMouseClicked(click -> {
            listTelefoni.getItems().clear();
            listTelefoni.getItems().addAll(linkedPersona.getTelefoni());
        });

        //listTelefoni.setOnEditCancel();
    }

    private void setupTableBambini()
    {
        TableColumn<Bambino, String> cNome = new TableColumn<>("Nome");
        TableColumn<Bambino, String> cCognome = new TableColumn<>("Congome");
        TableColumn<Bambino, String> cCodiceFiscale = new TableColumn<>("Codice Fiscale");
        TableColumn<Bambino, String> cMatricola = new TableColumn<>("Matricola");
        cNome.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getNome()));
        cCognome.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getCognome()));
        cCodiceFiscale.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getCodiceFiscale()));
        cMatricola.setCellValueFactory(c -> new ReadOnlyStringWrapper(String.valueOf(c.getValue().getID())));

        tableBambini.getColumns().addAll(cNome, cCognome, cCodiceFiscale, cMatricola);

        tableBambini.setOnMousePressed(click -> {
            if(click.isPrimaryButtonDown() && click.getClickCount() == 2 && tableBambini.getSelectionModel().getSelectedItem() != null)
                ShowSubPersona(tableBambini.getSelectionModel().getSelectedItem());
        });

        tableBambini.dragForClass(Bambino.class);
    }

    private void setupTableGenitori()
    {
        TableColumn<Genitore, String> cNome = new TableColumn<>("Nome");
        TableColumn<Genitore, String> cCognome = new TableColumn<>("Congome");
        TableColumn<Genitore, String> cCodiceFiscale = new TableColumn<>("Codice Fiscale");
        TableColumn<Genitore, String> cTelefoni = new TableColumn<>("Telefoni");
        cNome.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getNome()));
        cCognome.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getCognome()));
        cCodiceFiscale.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getCodiceFiscale()));
        cTelefoni.setCellValueFactory(c -> new ReadOnlyStringWrapper(EntitiesHelper.getTelefoniStringFromIterable(c.getValue().getTelefoni())));

        tableGenitori.getColumns().addAll(cNome, cCognome, cCodiceFiscale, cTelefoni);


        tableGenitori.setOnMousePressed(click -> {
            if(click.isPrimaryButtonDown() && click.getClickCount() == 2 && tableGenitori.getSelectionModel().getSelectedItem() != null)
                ShowSubPersona(tableGenitori.getSelectionModel().getSelectedItem());
        });

        tableGenitori.dragForClass(Genitore.class);
    }

    private void setupTablePediatra()
    {
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
    }

    private void setupContatti()
    {
        TableColumn<Contatto, String> cNomeContatto = new TableColumn<>("Nome");
        TableColumn<Contatto, String> cCognomeContatto = new TableColumn<>("Congome");
        TableColumn<Contatto, String> cDescrizioneContatto = new TableColumn<>("Descrizione");
        TableColumn<Contatto, String> cTelefoniContatto = new TableColumn<>("Telefoni");
        cNomeContatto.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getNome()));
        cCognomeContatto.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getCognome()));
        cDescrizioneContatto.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getDescrizione()));
        cTelefoniContatto.setCellValueFactory(c -> new ReadOnlyStringWrapper(EntitiesHelper.getTelefoniStringFromIterable(c.getValue().getTelefoni())));

        tableContatti.getColumns().addAll(cNomeContatto, cCognomeContatto, cDescrizioneContatto, cTelefoniContatto);

        tableContatti.dragForClass(Contatto.class);
    }

    private void updateLayout()
    {
        //Nasconde le tabelle inutili
        if(!(this.linkedPersona instanceof Bambino))
        {
            layoutTabPane.getTabs().remove(tabContatti);
            layoutTabPane.getTabs().remove(tabGenitori);
        }

        if(!(this.linkedPersona instanceof Genitore))
        {
            layoutTabPane.getTabs().remove(tabBambini);
        }

        for(Persona.ESesso sesso : Persona.ESesso.values())
            cbSesso.getItems().add(sesso.name());
    }

    private void printPersonaDetails()
    {

        if(this.linkedPersona != null)
        {
            //Setup title
            if(this.linkedPersona.getNome() != null)
                this.stageController.requestSetTitle(linkedPersona.getNome() + " " + linkedPersona.getCognome());
            else
                this.stageController.requestSetTitle("Crea " + linkedPersona.getClass().getSimpleName());

            //Setup fields
            txtNome.setTextFieldText(linkedPersona.getNome());
            txtCognome.setTextFieldText(linkedPersona.getCognome());
            txtCodiceFiscale.setTextFieldText(linkedPersona.getCodiceFiscale());
            txtCittadinanza.setTextFieldText(linkedPersona.getCittadinanza());
            txtComune.setTextFieldText(linkedPersona.getComune());
            txtProvincia.setTextFieldText(linkedPersona.getProvincia());
            txtResidenza.setTextFieldText(linkedPersona.getResidenza());
            txtStato.setTextFieldText(linkedPersona.getStato());
            if(linkedPersona.getSesso() != null)
                cbSesso.getSelectionModel().select(linkedPersona.getSesso().name());
            else
                cbSesso.getSelectionModel().select(2);
            dpDataNascita.setValue(linkedPersona.getDataNascita());
        }
    }

    @Override
    public void detached()
    {
        if(pendingOperation != null)
            ClientNetworkManager.getInstance().abortOperation(this.pendingOperation);
    }

    private void ShowSubPersona(Persona persona)
    {
        try {
            ChildcareBaseStageController setPresenzeStage = new ChildcareBaseStageController();
            setPresenzeStage.setContentScene(getClass().getClassLoader().getResource(EditPersona.FXML_PATH), persona);
            setPresenzeStage.initOwner(getRoot().getScene().getWindow());
            setPresenzeStage.initModality(Modality.WINDOW_MODAL);
            setPresenzeStage.setOnClosingCallback((returnArgs) -> {
                //TODO
                //Niente
            });
            setPresenzeStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
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
