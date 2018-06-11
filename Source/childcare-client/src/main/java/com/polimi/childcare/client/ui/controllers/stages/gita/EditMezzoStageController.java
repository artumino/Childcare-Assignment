package com.polimi.childcare.client.ui.controllers.stages.gita;

import com.polimi.childcare.client.shared.networking.NetworkOperation;
import com.polimi.childcare.client.ui.constants.ToolbarButtons;
import com.polimi.childcare.client.ui.controllers.BaseStageController;
import com.polimi.childcare.client.ui.controllers.ChildcareBaseStageController;
import com.polimi.childcare.client.ui.controllers.ISceneController;
import com.polimi.childcare.client.ui.controllers.ISubSceneController;
import com.polimi.childcare.client.ui.controllers.stages.generic.ReazioniAvverseStage;
import com.polimi.childcare.client.ui.controllers.stages.networking.BlockingNetworkOperationStageController;
import com.polimi.childcare.client.ui.controllers.subscenes.NetworkedSubScene;
import com.polimi.childcare.client.ui.controls.DragAndDropTableView;
import com.polimi.childcare.client.ui.controls.LabelTextViewComponent;
import com.polimi.childcare.client.ui.utils.StageUtils;
import com.polimi.childcare.shared.entities.*;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredMezzoDiTrasportoRequest;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredPastoRequest;
import com.polimi.childcare.shared.networking.requests.setters.SetMezzoDiTrasportoRequest;
import com.polimi.childcare.shared.networking.requests.setters.SetPastiRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListMezzoDiTrasportoResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListResponse;
import com.polimi.childcare.shared.utils.EntitiesHelper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Modality;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EditMezzoStageController extends NetworkedSubScene implements ISubSceneController
{
    public static final String FXML_PATH = "fxml/stages/gita/EditMezzoStage.fxml";

    private MezzoDiTrasporto linkedMezzo;
    private ChildcareBaseStageController stageController;
    private Parent root;

    @FXML private AnchorPane rootPane;
    @FXML private AnchorPane loadingLayout;

    @FXML private TabPane layoutTabPane;

    @FXML private LabelTextViewComponent txtTarga;
    @FXML private LabelTextViewComponent txtNumeroIdentificativo;
    @FXML private LabelTextViewComponent txtCostoOrario;
    @FXML private LabelTextViewComponent txtCapienza;
    @FXML private DragAndDropTableView<Fornitore> tableFornitore;

    @FXML private DragAndDropTableView<Gita> tableGite;

    @FXML private Button btnSalva;
    @FXML private Button btnElimina;
    @FXML private Tab tabGite;

    @Override
    public void attached(ISceneController sceneController, Object... args)
    {
        if(sceneController instanceof ChildcareBaseStageController)
            this.stageController = (ChildcareBaseStageController)sceneController;

        layoutTabPane.getTabs().remove(tabGite);

        if(this.stageController != null)
        {
            this.stageController.setToolbarButtonsVisibilityMask((byte)(ToolbarButtons.Close | ToolbarButtons.Minimize));

            if(args != null && args.length > 0 && args[0] instanceof MezzoDiTrasporto)
                this.linkedMezzo = (MezzoDiTrasporto) args[0];


            if(linkedMezzo != null && linkedMezzo.getID() != 0)
            {
                this.loadingLayout.setVisible(true);
                networkOperationVault.submitOperation(new NetworkOperation(new FilteredMezzoDiTrasportoRequest(this.linkedMezzo.getID(), true),
                        response ->
                        {
                            networkOperationVault.operationDone(FilteredPastoRequest.class);

                            if(StageUtils.HandleResponseError(response, "Errore, risposta non corretta dal server",
                                    p -> p instanceof ListMezzoDiTrasportoResponse && ((ListMezzoDiTrasportoResponse)p).getPayload().size() != 0))
                                stageController.requestClose();
                            else
                            {
                                this.linkedMezzo = ((ListMezzoDiTrasportoResponse)response).getPayload().get(0);
                                updateData();
                                this.loadingLayout.setVisible(false);
                            }
                        },
                        true));
            }

            //Tabelle Generali
            printMezzoDetails();
            setupGite();
            setupTableFornitori();

            //Bottoni
            if(linkedMezzo.getID() == 0) //Nascondo per persone nuove
                btnElimina.setVisible(false);

            btnElimina.setOnMouseClicked(click -> {
                    Optional<ButtonType> result = StageUtils.ShowAlertWithButtons(Alert.AlertType.CONFIRMATION,
                            "Sei sicuro di voler cancellare " + linkedMezzo.getTarga() + "?",
                            ButtonType.YES, ButtonType.NO);

                    if(result.isPresent() && result.get().equals(ButtonType.YES))
                    {
                        //Confermata la cancellazione
                        ShowBlockingNetworkOperationStage(new NetworkOperation(new SetMezzoDiTrasportoRequest(linkedMezzo, true, linkedMezzo.consistecyHashCode()),null, true), (resultArgs) -> {
                           if(resultArgs != null && resultArgs.length > 0 && resultArgs[0] instanceof BaseResponse)
                           {
                               BaseResponse response = (BaseResponse)resultArgs[0];

                               if(!(response instanceof BadRequestResponse))
                               {
                                   stageController.close();
                                   return;
                               }

                               StageUtils.HandleResponseError(response, "Errore nella cancellazione del mezzo", p -> true);
                           }
                        });
                    }
            });

            btnSalva.setOnMouseClicked(this::SaveClicked);

            //Aggiorna l'interfaccia con i presenti in linkedPersona
            updateData();
        }
    }

    private void updateData()
    {
        //Fornitore
        if(linkedMezzo != null && tableFornitore != null && linkedMezzo.getFornitore() != null)
        {
            tableFornitore.getItems().clear();
            tableFornitore.getItems().add(linkedMezzo.getFornitore());
        }

        //Gite
        /*if(this.linkedMezzo != null && tableGite != null && linkedMezzo.getPianoViaggi() != null)
        {
            tableGite.getItems().clear();
            List<Gita> gite = new ArrayList<>();
            for(PianoViaggi pianoViaggi : linkedMezzo.getPianoViaggi())
                gite.add(pianoViaggi.getGita());
            tableGite.getItems().addAll(gite);
        }*/

        printMezzoDetails();
    }

    private void setupGite()
    {
        TableColumn<Gita, Integer> cID = new TableColumn<>("ID");
        TableColumn<Gita, String> cLuogo = new TableColumn<>("Luogo");
        TableColumn<Gita, LocalDate> cInizio = new TableColumn<>("Data Inizio");
        TableColumn<Gita, LocalDate> cFine = new TableColumn<>("Data Fine");

        cID.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getID()));
        cLuogo.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getLuogo()));
        cInizio.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getDataInizio()));
        cInizio.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getDataFine()));


        if(tableGite != null)
            tableGite.getColumns().addAll(cID, cLuogo, cInizio, cFine);
    }

    private void setupTableFornitori()
    {
        TableColumn<Fornitore, String> cRagioneSociale = new TableColumn<>("Ragione Sociale");
        TableColumn<Fornitore, String> cSedeLegale = new TableColumn<>("Sede Legale");
        TableColumn<Fornitore, String> cTelefoni = new TableColumn<>("Telefoni");
        cRagioneSociale.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getRagioneSociale()));
        cSedeLegale.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getSedeLegale()));
        cTelefoni.setCellValueFactory(c -> new ReadOnlyStringWrapper(EntitiesHelper.getTelefoniStringFromIterable(c.getValue().getTelefoni())));


        if(tableFornitore != null)
        {
            tableFornitore.getColumns().addAll(cRagioneSociale, cSedeLegale, cTelefoni);
            tableFornitore.dragForClass(Fornitore.class);
        }
    }

    private void printMezzoDetails()
    {

        if(this.linkedMezzo != null)
        {
            //Setup title
            if(this.linkedMezzo.getTarga() != null)
                this.stageController.requestSetTitle(linkedMezzo.getTarga());
            else
                this.stageController.requestSetTitle("Crea " + linkedMezzo.getClass().getSimpleName());

            //Setup fields
            txtTarga.setTextFieldText(linkedMezzo.getTarga());
            txtNumeroIdentificativo.setTextFieldText(String.valueOf(linkedMezzo.getNumeroIdentificativo()));
            txtCostoOrario.setTextFieldText(String.valueOf(linkedMezzo.getCostoOrario()));
            txtCapienza.setTextFieldText(String.valueOf(linkedMezzo.getCapienza()));
        }
    }

    private void SaveClicked(MouseEvent ignored)
    {
        MezzoDiTrasporto newMezzo = new MezzoDiTrasporto();

        //Imposta dettagli
        newMezzo.unsafeSetID(linkedMezzo.getID());
        newMezzo.setTarga(txtTarga.getTextFieldText());

        try
        {
            newMezzo.setNumeroIdentificativo(Integer.parseInt(txtNumeroIdentificativo.getTextFieldText()));
            newMezzo.setCostoOrario(Integer.parseInt(txtCostoOrario.getTextFieldText()));
            newMezzo.setCapienza(Integer.parseInt(txtCapienza.getTextFieldText()));
        }
        catch (Exception ex)
        {
            StageUtils.ShowAlert(Alert.AlertType.ERROR, "Alcuni parametri numerici hanno valori non numerici!");
            return;
        }

        if(tableFornitore.getItems().size() > 0)
            newMezzo.setFornitore(tableFornitore.getItems().get(0));

        for (PianoViaggi pianoViaggi : linkedMezzo.getPianoViaggi())
            newMezzo.unsafeAddPianoViaggi(pianoViaggi);

        //Manda richiesta
        ShowBlockingNetworkOperationStage(new NetworkOperation(new SetMezzoDiTrasportoRequest(newMezzo, false, linkedMezzo.consistecyHashCode()), null, true), returnArgs ->
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

                    linkedMezzo = ((ListResponse<MezzoDiTrasporto>) response).getPayload().get(0);

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
