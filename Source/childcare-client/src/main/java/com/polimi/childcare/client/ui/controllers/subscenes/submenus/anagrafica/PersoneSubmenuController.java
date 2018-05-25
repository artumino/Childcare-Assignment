package com.polimi.childcare.client.ui.controllers.subscenes.submenus.anagrafica;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.polimi.childcare.client.shared.networking.ClientNetworkManager;
import com.polimi.childcare.client.shared.networking.NetworkOperation;
import com.polimi.childcare.client.shared.qrcode.BambinoQRUnit;
import com.polimi.childcare.client.ui.controllers.ISceneController;
import com.polimi.childcare.client.ui.utils.DateUtils;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.entities.Persona;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredPersonaRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListPersoneResponse;
import com.polimi.childcare.shared.serialization.SerializationUtils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.embed.swing.SwingFXUtils;
import javafx.print.PrinterJob;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import jdk.nashorn.internal.ir.annotations.Ignore;

import java.awt.image.BufferedImage;
import java.util.Base64;
import java.util.HashMap;

public class PersoneSubmenuController extends AnagraficaSubmenuBase<Persona>
{

    //Lista persone da visualizzare
    private ObservableList<Persona> listaPersone;
    private FilteredList<Persona> filteredPersone;
    private SortedList<Persona> sortedPersone;

    //Generated
    private TextField filterField;
    private Button btnPrintQR;

    //Watchers
    private Persona selectedPersona;

    //Network
    private NetworkOperation pendingOperation;

    @Override
    protected void initialize()
    {
        listaPersone = FXCollections.observableArrayList();

        //Imposto la scena
        TableColumn<Persona, String> name = new TableColumn<>("Nome");
        TableColumn<Persona, String> surname = new TableColumn<>("Cognome");
        TableColumn<Persona, String> fiscalCode = new TableColumn<>("Codice Fiscale");
        TableColumn<Persona, String> dateOfBirth = new TableColumn<>("Data di Nascita");
        TableColumn<Persona, Integer> id = new TableColumn<>("Matricola");

        name.setCellValueFactory((cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getNome()));
        surname.setCellValueFactory((cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getCognome()));
        fiscalCode.setCellValueFactory((cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getCodiceFiscale()));

        dateOfBirth.setCellValueFactory((cellData) -> new ReadOnlyStringWrapper(
                DateUtils.dateToShortString(cellData.getValue().getDataNascita())));

        id.setCellValueFactory((cellData) -> new ReadOnlyObjectWrapper<>(cellData.getValue().getID()));

        //Crea Lista filtrata ed imposto filtro
        filteredPersone = new FilteredList<>(listaPersone, p -> true);

        //Crea lista ordinata
        sortedPersone = new SortedList<>(filteredPersone);

        if(tableView != null) {
            tableView.getColumns().addAll(name, surname, fiscalCode, dateOfBirth, id);
            sortedPersone.comparatorProperty().bind(tableView.comparatorProperty());
            tableView.setItems(sortedPersone);
            //tableList.setColumnResizePolicy(p -> true);
        }

        vboxFilters.getChildren().clear();

        //Crea filtri
        filterField = new TextField();
        filterField.setPromptText("Filtra...");
        filterField.textProperty().addListener((observable, oldValue, newValue) ->
        {
            //Se diverso filtra, sennò accetta tutti i valori
            if(newValue != null && !newValue.isEmpty() && !oldValue.equals(newValue))
                filterList();
            else
                filteredPersone.setPredicate(p -> true);
        });
        vboxFilters.getChildren().add(filterField);

        tableView.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldPersona, newPersona) -> {
            selectedPersona = newPersona;
            redrawControls();
        }));
    }

    private void redrawControls()
    {
        vboxControls.getChildren().clear();

        //Crea Controlli
        if(selectedPersona instanceof Bambino)
        {
            btnPrintQR = new Button("Stampa QR");
            btnPrintQR.setOnMouseClicked((event) -> {
                if(selectedPersona instanceof Bambino)
                {
                    Bambino bambino = (Bambino)selectedPersona;
                    BambinoQRUnit qrUnit = new BambinoQRUnit(bambino);
                    byte[] data = SerializationUtils.serializeToByteArray(qrUnit);

                    if(data != null)
                    {
                        QRCodeWriter qrCodeWriter = new QRCodeWriter();
                        try {
                            BitMatrix bitMatrix = qrCodeWriter.encode(Base64.getEncoder().encodeToString(data), BarcodeFormat.QR_CODE, 512, 512);
                            ImageView qrView = new ImageView();

                            WritableImage image = SwingFXUtils.toFXImage(MatrixToImageWriter.toBufferedImage(bitMatrix),null);
                            qrView.setImage(image);
                            qrView.prefHeight(512);
                            qrView.prefWidth(512);

                            PrinterJob printerJob = PrinterJob.createPrinterJob();
                            if (printerJob != null && printerJob.showPrintDialog(this.rootPane.getScene().getWindow()))
                            {
                                boolean success = printerJob.printPage(qrView);
                                if (success) {
                                    printerJob.endJob();
                                }
                                else
                                    System.err.println("Errore durante la stampa...");
                            }
                            printerJob.printPage(qrView);
                        } catch (WriterException ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                }
            });
            vboxControls.getChildren().add(btnPrintQR);
        }

    }

    private void filterList()
    {
        if(filteredPersone != null)
        {
            filteredPersone.setPredicate(persona -> {
                String lowerCaseFilter = filterField.getText().toLowerCase();
                boolean spaced = lowerCaseFilter.contains(" ");

                try
                {
                    //Se è una matricola
                    Integer.parseInt(lowerCaseFilter);
                    return String.valueOf(persona.getID()).contains(lowerCaseFilter);
                }
                catch (Exception ex)
                {
                    //Se è qualcos'altro
                    if(persona.getNome().toLowerCase().contains(lowerCaseFilter))
                        return true;
                    if(persona.getCognome().toLowerCase().contains(lowerCaseFilter))
                        return true;
                    if(DateUtils.dateToShortString(persona.getDataNascita()).contains(lowerCaseFilter))
                        return true;
                    if(persona.getCodiceFiscale().toLowerCase().contains(lowerCaseFilter))
                        return true;
                    return spaced && ((persona.getNome().toLowerCase() + " " + persona.getCognome().toLowerCase()).contains(lowerCaseFilter)
                                        || (persona.getCognome().toLowerCase() + " " + persona.getNome().toLowerCase()).contains(lowerCaseFilter));
                }
            });
        }
    }

    @Override
    public void attached(ISceneController sceneController)
    {
        if(this.pendingOperation != null)
            ClientNetworkManager.getInstance().abortOperation(this.pendingOperation);

        this.pendingOperation = new NetworkOperation(
                new FilteredPersonaRequest(0, 0, false, null, new HashMap<>()),
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

        ListPersoneResponse bambiniResponse = (ListPersoneResponse)response;

        //Aggiorno i valori
        listaPersone.clear();

        //Aggiungo tutti i bambini
        if(bambiniResponse.getPayload() != null)
        {
            listaPersone.addAll(bambiniResponse.getPayload());
            tableView.sort();
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