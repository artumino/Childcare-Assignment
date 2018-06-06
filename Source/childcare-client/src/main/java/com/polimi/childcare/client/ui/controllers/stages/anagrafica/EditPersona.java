package com.polimi.childcare.client.ui.controllers.stages.anagrafica;

import com.polimi.childcare.client.shared.networking.ClientNetworkManager;
import com.polimi.childcare.client.shared.networking.NetworkOperation;
import com.polimi.childcare.client.ui.constants.ToolbarButtons;
import com.polimi.childcare.client.ui.controllers.BaseStageController;
import com.polimi.childcare.client.ui.controllers.ChildcareBaseStageController;
import com.polimi.childcare.client.ui.controllers.ISceneController;
import com.polimi.childcare.client.ui.controllers.ISubSceneController;
import com.polimi.childcare.client.ui.controllers.stages.generic.ReazioniAvverseStage;
import com.polimi.childcare.client.ui.controllers.stages.networking.BlockingNetworkOperationStageController;
import com.polimi.childcare.client.ui.controls.DragAndDropTableView;
import com.polimi.childcare.client.ui.controls.LabelTextViewComponent;
import com.polimi.childcare.client.ui.utils.StageUtils;
import com.polimi.childcare.shared.entities.*;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredPersonaRequest;
import com.polimi.childcare.shared.networking.requests.setters.SetBambinoRequest;
import com.polimi.childcare.shared.networking.requests.setters.SetPersonaRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.*;
import com.polimi.childcare.shared.utils.EntitiesHelper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

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
    @FXML private Button btnShowReazioniAvverse;

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


            if(pendingOperation == null && linkedPersona != null && linkedPersona.getID() != 0)
            {
                this.loadingLayout.setVisible(true);
                this.pendingOperation = new NetworkOperation(new FilteredPersonaRequest(this.linkedPersona.getID(), true),
                        response ->
                        {
                            this.pendingOperation = null;
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

            //Bottoni
            if(linkedPersona.getID() == 0) //Nascondo per persone nuove
                btnElimina.setVisible(false);

            btnElimina.setOnMouseClicked(click -> {
                if(this.pendingOperation == null)
                {
                    Optional<ButtonType> result = StageUtils.ShowAlertWithButtons(Alert.AlertType.CONFIRMATION,
                            "Sei sicuro di voler cancellare " + linkedPersona.getNome() + " " + linkedPersona.getCognome() + "?",
                            ButtonType.YES, ButtonType.NO);

                    if(result.isPresent() && result.get().equals(ButtonType.YES))
                    {
                        //Confermata la cancellazione
                        ShowBlockingNetworkOperationStage(new NetworkOperation(new SetPersonaRequest(linkedPersona, true, linkedPersona.consistecyHashCode()),null, true), (resultArgs) -> {
                           if(resultArgs != null && resultArgs.length > 0 && resultArgs[0] instanceof BaseResponse)
                           {
                               BaseResponse response = (BaseResponse)resultArgs[0];
                               if(!(response instanceof BadRequestResponse))
                               {
                                   stageController.close();
                                   return;
                               }

                               if(response instanceof BadRequestResponse.BadRequestResponseWithMessage)
                                   StageUtils.ShowAlert(Alert.AlertType.ERROR, ((BadRequestResponse.BadRequestResponseWithMessage)response).getMessage());
                               else
                                   StageUtils.ShowAlert(Alert.AlertType.ERROR, "Errore nella cancellazione della persona");
                           }
                        });
                    }
                }
            });

            btnSalva.setOnMouseClicked(this::SaveClicked);

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
                if(!empty && getTableRow().getItem() != null)
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


        btnShowReazioniAvverse.setOnMouseClicked(click -> ShowReazioniAvverse());

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

        if(cbSesso.getItems().size() == 0)
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
                cbSesso.getSelectionModel().select(Persona.ESesso.Altro.name());
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
            ChildcareBaseStageController personaDetails = new ChildcareBaseStageController();
            personaDetails.setContentScene(getClass().getClassLoader().getResource(EditPersona.FXML_PATH), persona);
            personaDetails.initOwner(getRoot().getScene().getWindow());
            personaDetails.initModality(Modality.WINDOW_MODAL);
            personaDetails.setOnClosingCallback((returnArgs) -> {
                //TODO
                //Niente
            });
            personaDetails.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ShowReazioniAvverse()
    {
        try {
            ChildcareBaseStageController showReazioniAvverse = new ChildcareBaseStageController();
            showReazioniAvverse.setContentScene(getClass().getClassLoader().getResource(ReazioniAvverseStage.FXML_PATH), linkedPersona);
            showReazioniAvverse.initOwner(getRoot().getScene().getWindow());
            //showReazioniAvverse.initModality(Modality.APPLICATION_MODAL); //Blocco tutto
            showReazioniAvverse.setOnClosingCallback((returnArgs) -> {
                //Niente
            });
            showReazioniAvverse.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ShowBlockingNetworkOperationStage(NetworkOperation networkOperation, BaseStageController.OnStageClosingCallback callback)
    {
        try {
            ChildcareBaseStageController blockingOperationController = new ChildcareBaseStageController();
            blockingOperationController.setContentScene(getClass().getClassLoader().getResource(BlockingNetworkOperationStageController.FXML_PATH), networkOperation);
            blockingOperationController.initOwner(getRoot().getScene().getWindow());
            blockingOperationController.initModality(Modality.APPLICATION_MODAL); //Blocco tutto
            blockingOperationController.setOnClosingCallback(callback);
            blockingOperationController.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void SaveClicked(MouseEvent ignored)
    {
        if(pendingOperation != null)
            return;

        Persona newPersona = null;

        if(linkedPersona instanceof Bambino)
            newPersona = new Bambino();
        else if(linkedPersona instanceof Genitore)
            newPersona = new Genitore();
        else
            newPersona = new Addetto();

        //Imposta dettagli
        newPersona.unsafeSetID(linkedPersona.getID());
        newPersona.setNome(txtNome.getTextFieldText());
        newPersona.setCognome(txtCognome.getTextFieldText());
        newPersona.setCodiceFiscale(txtCodiceFiscale.getTextFieldText());
        newPersona.setDataNascita(dpDataNascita.getValue());
        newPersona.setStato(txtStato.getTextFieldText());
        newPersona.setComune(txtComune.getTextFieldText());
        newPersona.setProvincia(txtProvincia.getTextFieldText());
        newPersona.setCittadinanza(txtCittadinanza.getTextFieldText());
        newPersona.setResidenza(txtResidenza.getTextFieldText());
        newPersona.setSesso(Persona.ESesso.valueOf(cbSesso.getSelectionModel().getSelectedItem()));

        //Imposta relazioni persona
        for(Diagnosi diagnosi : tableDiagnosi.getItems())
            newPersona.unsafeAddDiagnosi(diagnosi);

        for(String telefono : listTelefoni.getItems())
            newPersona.addTelefono(telefono);

        //Relazioni Specifiche
        if(newPersona instanceof Bambino)
        {
            Bambino newBambino = (Bambino)newPersona;

            for(Genitore genitore : tableGenitori.getItems())
                newBambino.addGenitore(genitore);

            for(Contatto contatto : tableContatti.getItems())
                newBambino.unsafeAddContatto(contatto);

            newBambino.setGruppo(((Bambino) linkedPersona).getGruppo());
            newBambino.setPediatra(tablePediatra.getItems().size() == 0 ? null : tablePediatra.getItems().get(0));
        }

        if(newPersona instanceof Genitore)
        {
            Genitore newGenitore = (Genitore)newPersona;

            for(Bambino bambino : tableBambini.getItems())
                newGenitore.unsafeAddBambino(bambino);
        }

        //Manda richiesta
        ShowBlockingNetworkOperationStage(new NetworkOperation(new SetPersonaRequest(newPersona, false, linkedPersona.consistecyHashCode()), null, true), returnArgs ->
        {
            if(returnArgs != null && returnArgs.length > 0 && returnArgs[0] instanceof BaseResponse)
            {
                BaseResponse response = (BaseResponse)returnArgs[0];
                String errorMessage = "Impossibile eseguire l'operazione di modifica/inserimento, si è verificato un errore sconosciuto.";
                if(response.getCode() != 200 && !(response instanceof ListResponse))
                {

                    if(response instanceof BadRequestResponse.BadRequestResponseWithMessage)
                        errorMessage = ((BadRequestResponse.BadRequestResponseWithMessage) response).getMessage();

                    StageUtils.ShowAlert(Alert.AlertType.ERROR, errorMessage);
                    return;
                }

                if(response instanceof ListResponse)
                {
                    if(((ListResponse) response).getPayload() == null || ((ListResponse) response).getPayload().size() == 0) {
                        StageUtils.ShowAlert(Alert.AlertType.ERROR, errorMessage);
                        return;
                    }

                    StageUtils.ShowAlert(Alert.AlertType.INFORMATION, "La modifica non è stata effettuata perchè i dati non sono consistenti, rieffetuare le modifiche...");

                    if(linkedPersona instanceof Bambino)
                        this.linkedPersona = ((ListResponse<Bambino>) response).getPayload().get(0);
                    else if(linkedPersona instanceof Genitore)
                        this.linkedPersona = ((ListResponse<Genitore>) response).getPayload().get(0);
                    else if(linkedPersona instanceof Addetto)
                        this.linkedPersona = ((ListResponse<Addetto>) response).getPayload().get(0);

                    updateData();
                    return;
                }

                stageController.requestClose();
            }
        });
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
