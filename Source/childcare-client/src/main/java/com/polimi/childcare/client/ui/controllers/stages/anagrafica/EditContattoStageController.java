package com.polimi.childcare.client.ui.controllers.stages.anagrafica;

import com.polimi.childcare.client.shared.networking.ClientNetworkManager;
import com.polimi.childcare.client.shared.networking.NetworkOperation;
import com.polimi.childcare.client.ui.constants.ToolbarButtons;
import com.polimi.childcare.client.ui.controllers.BaseStageController;
import com.polimi.childcare.client.ui.controllers.ChildcareBaseStageController;
import com.polimi.childcare.client.ui.controllers.ISceneController;
import com.polimi.childcare.client.ui.controllers.ISubSceneController;
import com.polimi.childcare.client.ui.controllers.stages.networking.BlockingNetworkOperationStageController;
import com.polimi.childcare.client.ui.controllers.subscenes.NetworkedSubScene;
import com.polimi.childcare.client.ui.controls.DragAndDropTableView;
import com.polimi.childcare.client.ui.controls.LabelTextViewComponent;
import com.polimi.childcare.client.ui.utils.StageUtils;
import com.polimi.childcare.shared.entities.*;
import com.polimi.childcare.shared.networking.requests.BaseRequest;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredContattoRequest;
import com.polimi.childcare.shared.networking.requests.setters.SetContattoRequest;
import com.polimi.childcare.shared.networking.requests.setters.SetPediatraRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListContattoResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListResponse;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Modality;

import java.io.IOException;
import java.util.Optional;

public class EditContattoStageController extends NetworkedSubScene implements ISubSceneController
{
    public static final String FXML_PATH = "fxml/stages/anagrafica/EditContattoStage.fxml";

    private Contatto linkedContatto;
    private ChildcareBaseStageController stageController;
    private Parent root;

    @FXML private AnchorPane rootPane;
    @FXML private AnchorPane loadingLayout;

    @FXML private TabPane layoutTabPane;
    @FXML private Tab tabDettagli;
    @FXML private Tab tabBambini;
    @FXML private Tab tabTelefoni;

    @FXML private LabelTextViewComponent txtNome;
    @FXML private LabelTextViewComponent txtCognome;
    @FXML private TextArea txtDescrizione;
    @FXML private LabelTextViewComponent txtIndirizzo;

    @FXML private DragAndDropTableView<Bambino> tableBambini;

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

            if(args != null && args.length > 0 && args[0] instanceof Contatto)
                this.linkedContatto = (Contatto)args[0];


            if(!networkOperationVault.operationRunning(FilteredContattoRequest.class) && linkedContatto != null && linkedContatto.getID() != 0)
            {
                this.loadingLayout.setVisible(true);
                networkOperationVault.submitOperation(new NetworkOperation(new FilteredContattoRequest(this.linkedContatto.getID(), true),
                        response ->
                        {
                            networkOperationVault.operationDone(FilteredContattoRequest.class);
                            if(!(response instanceof ListContattoResponse) || ((ListContattoResponse)response).getPayload().size() == 0)
                            {
                                StageUtils.ShowAlert(Alert.AlertType.ERROR, "Errore, risposta non corretta dal server");
                                stageController.requestClose();
                            }
                            else
                            {
                                this.linkedContatto = ((ListContattoResponse)response).getPayload().get(0);
                                updateData();
                                this.loadingLayout.setVisible(false);
                            }
                        },
                        true));
            }

            //Tabelle Generali
            printDetails();
            setupPhoneList();

            //Pediatra
            setupTableBambini();

            //Bottoni
            if(linkedContatto.getID() == 0) //Nascondo per persone nuove
                btnElimina.setVisible(false);

            btnElimina.setOnMouseClicked(click -> {
                if(!networkOperationVault.anyRunningOperation())
                {
                    Optional<ButtonType> result = StageUtils.ShowAlertWithButtons(Alert.AlertType.CONFIRMATION,
                            "Sei sicuro di voler cancellare " + linkedContatto.getNome() + " " + linkedContatto.getCognome() + "?",
                            ButtonType.YES, ButtonType.NO);

                    if(result.isPresent() && result.get().equals(ButtonType.YES))
                    {
                        BaseRequest request = null;
                        if(linkedContatto instanceof Pediatra)
                            request = new SetPediatraRequest((Pediatra)linkedContatto, true, linkedContatto.consistecyHashCode());
                        else
                            request = new SetContattoRequest(linkedContatto, true, linkedContatto.consistecyHashCode());

                        //Confermata la cancellazione
                        ShowBlockingNetworkOperationStage(new NetworkOperation(request,null, true), (resultArgs) -> {
                           if(resultArgs != null && resultArgs.length > 0 && resultArgs[0] instanceof BaseResponse)
                           {
                               BaseResponse response = (BaseResponse)resultArgs[0];
                               if(!(response instanceof BadRequestResponse))
                               {
                                   stageController.requestClose();
                                   return;
                               }

                               if(response instanceof BadRequestResponse.BadRequestResponseWithMessage)
                                   StageUtils.ShowAlert(Alert.AlertType.ERROR, ((BadRequestResponse.BadRequestResponseWithMessage)response).getMessage());
                               else
                                   StageUtils.ShowAlert(Alert.AlertType.ERROR, "Errore nella cancellazione del contatto");
                           }
                        });
                    }
                }
            });

            btnSalva.setOnMouseClicked(this::SaveClicked);

            //Aggiorna l'interfaccia con i presenti in linkedContatto
            updateData();
        }
    }

    private void updateData()
    {
        //Telefoni
        if(this.linkedContatto != null && listTelefoni != null && this.linkedContatto.getTelefoni() != null)
        {
            listTelefoni.getItems().clear();
            listTelefoni.getItems().addAll(this.linkedContatto.getTelefoni());
        }

        //Bambini
        if((this.linkedContatto instanceof Pediatra) && tableBambini != null && ((Pediatra) this.linkedContatto).getBambiniCurati() != null)
        {
            tableBambini.getItems().clear();
            tableBambini.getItems().addAll(((Pediatra) linkedContatto).getBambiniCurati());
        }

        updateLayout();
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
            listTelefoni.getItems().addAll(linkedContatto.getTelefoni());
        });

        //listTelefoni.setOnEditCancel();
    }

    private void setupTableBambini()
    {
        TableColumn<Bambino, String> cNome = new TableColumn<>("Nome");
        TableColumn<Bambino, String> cCognome = new TableColumn<>("Cognome");
        TableColumn<Bambino, String> cCodiceFiscale = new TableColumn<>("Codice Fiscale");
        TableColumn<Bambino, Integer> cMatricola = new TableColumn<>("Matricola");
        cNome.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getNome()));
        cCognome.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getCognome()));
        cCodiceFiscale.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getCodiceFiscale()));
        cMatricola.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getID()));

        tableBambini.getColumns().addAll(cNome, cCognome, cCodiceFiscale, cMatricola);

        tableBambini.setOnMousePressed(click -> {
            if(click.isPrimaryButtonDown() && click.getClickCount() == 2 && tableBambini.getSelectionModel().getSelectedItem() != null)
                ShowSubPersona(tableBambini.getSelectionModel().getSelectedItem());
        });

        tableBambini.dragForClass(Bambino.class);
    }

    private void updateLayout()
    {
        //Nasconde le tabelle inutili
        if(!(this.linkedContatto instanceof Pediatra))
            layoutTabPane.getTabs().remove(tabBambini);
    }

    private void printDetails()
    {

        if(this.linkedContatto != null)
        {
            //Setup title
            if(this.linkedContatto.getNome() != null)
                this.stageController.requestSetTitle(linkedContatto.getNome() + " " + linkedContatto.getCognome());
            else
                this.stageController.requestSetTitle("Crea " + linkedContatto.getClass().getSimpleName());

            //Setup fields
            txtNome.setTextFieldText(linkedContatto.getNome());
            txtCognome.setTextFieldText(linkedContatto.getCognome());
            txtIndirizzo.setTextFieldText(linkedContatto.getIndirizzo());
            txtDescrizione.setText(linkedContatto.getDescrizione());
        }
    }

    private void ShowSubPersona(Persona persona)
    {
        try {
            ChildcareBaseStageController personaDetails = new ChildcareBaseStageController();
            personaDetails.setContentScene(getClass().getClassLoader().getResource(EditContattoStageController.FXML_PATH), persona);
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

    private void SaveClicked(MouseEvent ignored)
    {
        if(networkOperationVault.anyRunningOperation())
            return;

        Contatto newContatto = null;

        if(linkedContatto instanceof Pediatra)
            newContatto = new Pediatra();
        else
            newContatto = new Contatto();

        //Imposta dettagli
        newContatto.unsafeSetID(linkedContatto.getID());
        newContatto.setNome(txtNome.getTextFieldText());
        newContatto.setCognome(txtCognome.getTextFieldText());
        newContatto.setIndirizzo(txtIndirizzo.getTextFieldText());
        newContatto.setDescrizione(txtDescrizione.getText());

        for(String telefono : listTelefoni.getItems())
            newContatto.addTelefono(telefono);

        //Mantiene i bambini come sono
        for(Bambino bambino : linkedContatto.getBambini())
            newContatto.addBambino(bambino);

        //Relazioni Specifiche
        if(newContatto instanceof Pediatra)
        {
            Pediatra newPediatra = (Pediatra) newContatto;

            for(Bambino bambino : tableBambini.getItems())
                newPediatra.unsafeAddBambino(bambino);
        }

        BaseRequest request = null;

        if(newContatto instanceof Pediatra)
            request = new SetPediatraRequest((Pediatra)newContatto, false, linkedContatto.consistecyHashCode());
        else
            request = new SetContattoRequest(newContatto, false, linkedContatto.consistecyHashCode());

        //Manda richiesta
        ShowBlockingNetworkOperationStage(new NetworkOperation(request, null, true), returnArgs ->
        {
            if(returnArgs != null && returnArgs.length > 0 && returnArgs[0] instanceof BaseResponse)
            {
                BaseResponse response = (BaseResponse)returnArgs[0];
                String errorMessage = "Impossibile eseguire l'operazione di modifica/inserimento, si è verificato un errore sconosciuto.";

                if(StageUtils.HandleResponseError(response, errorMessage, p -> p.getCode() == 200 || p instanceof ListResponse))
                    return;

                if(response instanceof ListResponse)
                {
                    if(((ListResponse) response).getPayload() == null || ((ListResponse) response).getPayload().size() == 0) {
                        StageUtils.ShowAlert(Alert.AlertType.ERROR, errorMessage);
                        return;
                    }

                    StageUtils.ShowAlert(Alert.AlertType.INFORMATION, "La modifica non è stata effettuata perchè i dati non sono consistenti, rieffetuare le modifiche...");

                    if(linkedContatto instanceof Pediatra)
                        this.linkedContatto = ((ListResponse<Pediatra>) response).getPayload().get(0);
                    else
                        this.linkedContatto = ((ListResponse<Contatto>) response).getPayload().get(0);

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
