package com.polimi.childcare.client.ui.controllers.stages.gita;

import com.polimi.childcare.client.shared.networking.NetworkOperation;
import com.polimi.childcare.client.ui.constants.ToolbarButtons;
import com.polimi.childcare.client.ui.controllers.ChildcareBaseStageController;
import com.polimi.childcare.client.ui.controllers.ISceneController;
import com.polimi.childcare.client.ui.controllers.ISubSceneController;
import com.polimi.childcare.client.ui.controllers.subscenes.NetworkedSubScene;
import com.polimi.childcare.client.ui.controls.GruppoContainerComponent;
import com.polimi.childcare.client.ui.controls.PianoViaggiComponent;
import com.polimi.childcare.client.ui.utils.StageUtils;
import com.polimi.childcare.shared.entities.*;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredGitaRequest;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredGruppoRequest;
import com.polimi.childcare.shared.networking.requests.setters.SetGitaRequest;
import com.polimi.childcare.shared.networking.requests.special.GeneratePianiViaggioRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListGitaResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListGruppoResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListResponse;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class EditPianoViaggiController extends NetworkedSubScene implements ISubSceneController
{
    public static final String FXML_PATH = "fxml/stages/gita/EditPianoViaggiStage.fxml";

    private Gita linkedGita;
    private List<PianoViaggiComponent> pianoViaggiComponents;
    private List<PianoViaggi> recivedPianoViaggi;
    private List<Gruppo> recivedGruppo;
    private ChildcareBaseStageController stageController;
    private Parent root;

    @FXML private AnchorPane rootPane;
    @FXML private AnchorPane loadingLayout;

    @FXML private TabPane layoutTabPane;

    @FXML private VBox vboxPianiViaggio;

    @FXML private Button btnSalva;

    @Override
    public void attached(ISceneController sceneController, Object... args)
    {
        if(sceneController instanceof ChildcareBaseStageController)
            this.stageController = (ChildcareBaseStageController)sceneController;


        if(this.stageController != null)
        {
            this.stageController.setToolbarButtonsVisibilityMask((byte)(ToolbarButtons.Close | ToolbarButtons.Minimize));
            this.stageController.requestSetTitle("Piano Viaggi");

            if(args != null && args.length > 0 && args[0] instanceof Gita)
                this.linkedGita = (Gita) args[0];


            if(linkedGita != null && linkedGita.getID() != 0)
            {
                this.loadingLayout.setVisible(true);

                pianoViaggiComponents = new ArrayList<>();

                networkOperationVault.submitOperation(new NetworkOperation(new FilteredGitaRequest(this.linkedGita.getID(), true),
                        response ->
                        {
                            networkOperationVault.operationDone(FilteredGitaRequest.class);

                            if(StageUtils.HandleResponseError(response, "Errore, risposta non corretta dal server",
                                    p -> p instanceof ListGitaResponse && ((ListGitaResponse)p).getPayload().size() != 0))
                                stageController.requestClose();
                            else
                            {
                                this.linkedGita = ((ListGitaResponse)response).getPayload().get(0);
                                refreshPianoViaggi(new ArrayList<>(this.linkedGita.getPianiViaggi()));
                            }
                        },
                        true));
            }

            btnSalva.setOnMouseClicked(this::SaveClicked);
        }
    }

    private void refreshPianoViaggi(List<PianoViaggi> pianoViaggi)
    {
        recivedPianoViaggi = pianoViaggi;
        pianoViaggiComponents.clear();
        for (PianoViaggi pianoviaggio : pianoViaggi)
            addPianoViaggiComponentForPianoViaggi(pianoviaggio);
        redrawGruppi();

        networkOperationVault.submitOperation(new NetworkOperation(new FilteredGruppoRequest(0, 0, false), response ->
        {
            networkOperationVault.operationDone(FilteredGruppoRequest.class);

            if(StageUtils.HandleResponseError(response, "Errore, risposta non corretta dal server",
                    p -> p instanceof ListGruppoResponse && ((ListGruppoResponse)p).getPayload().size() != 0))
                stageController.requestClose();
            else
            {
                updatePianoViaggi(((ListGruppoResponse)response).getPayload());
                this.loadingLayout.setVisible(false);
            }
        }, true));
    }

    private void updatePianoViaggi(List<Gruppo> gruppi)
    {
        recivedGruppo = gruppi;
        for (Gruppo gruppo : gruppi)
            addPianoViaggiComponentForGruppo(gruppo);
        redrawGruppi();
    }

    private void redrawGruppi()
    {
        if(vboxPianiViaggio != null)
        {
            vboxPianiViaggio.getChildren().clear();
            pianoViaggiComponents.sort(Comparator.comparingInt(o -> o.getLinkedGruppo().getID()));
            for (PianoViaggiComponent containerComponent : pianoViaggiComponents)
                vboxPianiViaggio.getChildren().add(containerComponent);
        }
    }

    private void addPianoViaggiComponentForPianoViaggi(PianoViaggi pianoViaggi)
    {
        PianoViaggiComponent containerComponent = new PianoViaggiComponent();
        containerComponent.bindMezzo(pianoViaggi.getMezzo());

        if(pianoViaggi.getGruppo() != null)
            containerComponent.bindGruppo(pianoViaggi.getGruppo());
        else {
            Gruppo dummy = new Gruppo();
            dummy.unsafeSetID(pianoViaggi.getGruppoForeignKey());
            containerComponent.bindGruppo(dummy);
        }

        vboxPianiViaggio.getChildren().add(containerComponent);
        pianoViaggiComponents.add(containerComponent);
    }

    private void addPianoViaggiComponentForGruppo(Gruppo gruppo)
    {
        for (PianoViaggiComponent pianoViaggiC : pianoViaggiComponents)
            if(pianoViaggiC.getLinkedGruppo().equals(gruppo))
            {
                pianoViaggiC.bindGruppo(gruppo); //Aggiorno la rappresentazione del gruppo
                return;
            }

        //Piano viaggi per il gruppo non presente
        PianoViaggiComponent containerComponent = new PianoViaggiComponent();
        containerComponent.bindGruppo(gruppo);
        containerComponent.bindMezzo(null);

        vboxPianiViaggio.getChildren().add(containerComponent);
        pianoViaggiComponents.add(containerComponent);
    }


    private void SaveClicked(MouseEvent ignored)
    {
        HashMap<Gruppo, MezzoDiTrasporto> gruppoMezzoDiTrasportoHashMap = new HashMap<>(pianoViaggiComponents.size());

        for(PianoViaggiComponent component : pianoViaggiComponents)
            gruppoMezzoDiTrasportoHashMap.put(component.getLinkedGruppo(), component.getLinkedMezzo());

        //Manda richiesta
        ShowBlockingNetworkOperationStage(new NetworkOperation(new GeneratePianiViaggioRequest(linkedGita, gruppoMezzoDiTrasportoHashMap),
                null, true), returnArgs ->
        {
            if(returnArgs != null && returnArgs.length > 0 && returnArgs[0] instanceof BaseResponse)
            {
                BaseResponse response = (BaseResponse)returnArgs[0];
                String errorMessage = "Impossibile eseguire l'operazione di modifica/inserimento, si è verificato un errore sconosciuto.";

                if(StageUtils.HandleResponseError(response, errorMessage, p -> p.getCode() == 200))
                    return;

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
