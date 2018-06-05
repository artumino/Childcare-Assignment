package com.polimi.childcare.client.ui.controllers.subscenes.submenus.anagrafica;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.jfoenix.controls.JFXButton;
import com.polimi.childcare.client.shared.networking.ClientNetworkManager;
import com.polimi.childcare.client.shared.networking.NetworkOperation;
import com.polimi.childcare.client.shared.qrcode.BambinoQRUnit;
import com.polimi.childcare.client.ui.controllers.ChildcareBaseStageController;
import com.polimi.childcare.client.ui.controllers.ISceneController;
import com.polimi.childcare.client.ui.controllers.stages.anagrafica.EditPersona;
import com.polimi.childcare.client.ui.filters.Filters;
import com.polimi.childcare.client.ui.utils.DateUtils;
import com.polimi.childcare.client.ui.utils.StageUtils;
import com.polimi.childcare.shared.entities.Addetto;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.entities.Genitore;
import com.polimi.childcare.shared.entities.Persona;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredBaseRequest;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredPersonaRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListPersoneResponse;
import com.polimi.childcare.shared.serialization.SerializationUtils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.function.Predicate;

public class PersoneSubmenuController extends AnagraficaSubmenuBase<Persona>
{
    //Generated
    private TextField filterField;
    private ComboBox<String> filterBox;
    private Button btnPrintQR;
    private Button btnUpdate;
    private Button btnAddBambino;
    private Button btnAddAddetto;
    private Button btnAddGenitore;

    //Network
    private NetworkOperation pendingOperation;

    @Override
    protected List<TableColumn<Persona, ?>> setupColumns()
    {
        TableColumn<Persona, String> name = new TableColumn<>("Nome");
        TableColumn<Persona, String> surname = new TableColumn<>("Cognome");
        TableColumn<Persona, String> fiscalCode = new TableColumn<>("Codice Fiscale");
        TableColumn<Persona, String> dateOfBirth = new TableColumn<>("Data di Nascita");
        TableColumn<Persona, Integer> id = new TableColumn<>("Matricola");
        TableColumn<Persona, String> type = new TableColumn<>("Tipo");

        name.setCellValueFactory((cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getNome()));
        name.setMinWidth(75);
        surname.setCellValueFactory((cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getCognome()));
        surname.setMinWidth(75);
        fiscalCode.setCellValueFactory((cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getCodiceFiscale()));
        fiscalCode.setMinWidth(75);

        dateOfBirth.setCellValueFactory((cellData) -> new ReadOnlyStringWrapper(
                DateUtils.dateToShortString(cellData.getValue().getDataNascita())));
        dateOfBirth.setMinWidth(75);

        id.setCellValueFactory((cellData) -> new ReadOnlyObjectWrapper<>(cellData.getValue().getID()));
        id.setMinWidth(75);

        type.setCellValueFactory((cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getClass().getSimpleName())));
        type.setPrefWidth(75);
        type.setMaxWidth(75);
        type.setMinWidth(75);


        tableView.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2)
                ShowPersonaDetails(this.selectedItem);
        });

        return Arrays.asList(name, surname, fiscalCode, dateOfBirth, id, type);
    }

    @Override
    protected void setupFilterNodes()
    {
        //Crea filtri
        filterField = new TextField();
        filterField.setPromptText("Filtra...");

        filterBox = new ComboBox<>(FXCollections.observableArrayList("Tutti", "Bambini", "Genitori", "Addetti"));
        filterBox.getSelectionModel().select(0);
        filterBox.setMaxWidth(Double.MAX_VALUE); //Fill

        filterComponent.addFilterField(filterField.textProperty(), (persona -> Filters.filterPersona(persona, filterField.getText())));
        filterComponent.addFilterField(filterBox.getSelectionModel().selectedIndexProperty(), persona -> {
            switch(filterBox.getSelectionModel().getSelectedIndex())
            {
                case 1:
                    return persona instanceof Bambino;
                case 2:
                    return persona instanceof Genitore;
                case 3:
                    return persona instanceof Addetto;
            }
            //Case "Tutti"
            return true;
        });
    }

    @Override
    protected void setupControlNodes()
    {
        if(btnUpdate == null)
        {
            btnUpdate = new JFXButton("Aggiorna");
            btnUpdate.setMaxWidth(Double.MAX_VALUE);
            btnUpdate.setOnMouseClicked(event -> refreshData());
        }

        if(btnPrintQR == null)
        {
            btnPrintQR = new JFXButton("Stampa QR");
            btnPrintQR.setMaxWidth(Double.MAX_VALUE); //Fill
            btnPrintQR.setOnMouseClicked(this::OnPrintQRCodeClicked);
        }

        if(btnAddBambino == null)
        {
            btnAddBambino = new JFXButton("Aggiungi Bambino");
            btnAddBambino.setMaxWidth(Double.MAX_VALUE); //Fill
            btnAddBambino.setOnMouseClicked(this::OnAddPersonaClicked);
        }

        if(btnAddAddetto == null)
        {
            btnAddAddetto = new JFXButton("Aggiungi Addetto");
            btnAddAddetto.setMaxWidth(Double.MAX_VALUE); //Fill
            btnAddAddetto.setOnMouseClicked(this::OnAddPersonaClicked);
        }

        if(btnAddGenitore == null)
        {
            btnAddGenitore = new JFXButton("Aggiungi Genitore");
            btnAddGenitore.setMaxWidth(Double.MAX_VALUE); //Fill
            btnAddGenitore.setOnMouseClicked(this::OnAddPersonaClicked);
        }
    }

    @Override
    protected Collection<Node> getShownFilterElements()
    {
        return Arrays.asList(filterField, filterBox);
    }

    @Override
    protected Collection<Node> getShownControlElements()
    {
        ArrayList<Node> controlNodes = new ArrayList<>(2);

        controlNodes.add(btnUpdate);
        controlNodes.add(btnAddAddetto);
        controlNodes.add(btnAddGenitore);
        controlNodes.add(btnAddBambino);
        if(selectedItem instanceof Bambino)
            controlNodes.add(btnPrintQR);

        return controlNodes;
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
                new FilteredPersonaRequest(0, 0, false),
                this::OnPersoneResponseRecived,
                true);

        //Provo ad aggiornare i dati
        ClientNetworkManager.getInstance().submitOperation(this.pendingOperation);
    }

    private void OnPersoneResponseRecived(BaseResponse response)
    {
        this.pendingOperation = null;

        if(!(response instanceof ListPersoneResponse))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Errore nell'aggiornare i dati: " + (response != null ? "Bad Request" : "Risposta Nulla"));
            alert.showAndWait();
            return;
        }

        ListPersoneResponse personeResponse = (ListPersoneResponse)response;
        filteredList.updateDataSet(personeResponse.getPayload());
        tableView.refresh();
    }

    private void OnPrintQRCodeClicked(MouseEvent event)
    {
        if (selectedItem instanceof Bambino)
        {
            Bambino bambino = (Bambino) selectedItem;
            BambinoQRUnit qrUnit = new BambinoQRUnit(bambino);
            byte[] data = SerializationUtils.serializeToByteArray(qrUnit);

            if (data != null) {
                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                try {
                    BitMatrix bitMatrix = qrCodeWriter.encode(Base64.getEncoder().encodeToString(data), BarcodeFormat.QR_CODE, 512, 512);
                    ImageView qrView = new ImageView();

                    WritableImage image = SwingFXUtils.toFXImage(MatrixToImageWriter.toBufferedImage(bitMatrix), null);
                    qrView.setImage(image);
                    qrView.prefHeight(512);
                    qrView.prefWidth(512);

                    PrinterJob printerJob = PrinterJob.createPrinterJob();
                    if (printerJob != null && printerJob.showPrintDialog(this.rootPane.getScene().getWindow())) {
                        boolean success = printerJob.printPage(qrView);
                        if (success) {
                            printerJob.endJob();
                        } else
                            System.err.println("Errore durante la stampa...");
                    }
                    printerJob.printPage(qrView);
                } catch (WriterException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void OnAddPersonaClicked(MouseEvent event)
    {
        if(event.getSource() == btnAddBambino)
            ShowPersonaDetails(new Bambino());

        if(event.getSource() == btnAddAddetto)
            ShowPersonaDetails(new Addetto());

        if(event.getSource() == btnAddGenitore)
            ShowPersonaDetails(new Genitore());
    }

    private void ShowPersonaDetails(Persona persona)
    {
        try {
            ChildcareBaseStageController setPresenzeStage = new ChildcareBaseStageController();
            setPresenzeStage.setContentScene(getClass().getClassLoader().getResource(EditPersona.FXML_PATH), persona);
            //setPresenzeStage.initOwner(getRoot().getScene().getWindow());
            setPresenzeStage.setOnClosingCallback((returnArgs) -> {
                //Niente
            });
            setPresenzeStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void detached()
    {
        if(this.pendingOperation != null)
            ClientNetworkManager.getInstance().abortOperation(this.pendingOperation);
        this.pendingOperation = null;
    }
}
