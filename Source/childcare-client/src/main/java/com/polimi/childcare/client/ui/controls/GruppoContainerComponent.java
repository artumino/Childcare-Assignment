package com.polimi.childcare.client.ui.controls;

import com.polimi.childcare.client.ui.OrderedFilteredList;
import com.polimi.childcare.client.ui.components.FilterComponent;
import com.polimi.childcare.client.ui.controls.events.IElementDragDropEvent;
import com.polimi.childcare.client.ui.filters.Filters;
import com.polimi.childcare.shared.entities.Addetto;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.entities.Gruppo;
import com.polimi.childcare.shared.entities.MezzoDiTrasporto;
import com.polimi.childcare.shared.utils.EntitiesHelper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * Controllo grafico per la visualizzazione e modifca dei gruppi
 */
public class GruppoContainerComponent extends AnchorPane
{
    private Gruppo linkedGruppo;
    private MezzoDiTrasporto linkedMezzo;
    private OrderedFilteredList<Bambino> listBambini;
    private FilterComponent<Bambino> filterBambini;

    //Events
    private IElementDragDropEvent<Bambino> onBambinoAggiunto;
    private IElementDragDropEvent<Bambino> onBambinoRimosso;

    @FXML private Label lblGroupName;
    @FXML private TextField txtFilterBambini;
    @FXML private DragAndDropTableView<Addetto> tableSorvegliante;
    @FXML private DragAndDropTableView<Bambino> tableBambini;
    @FXML private ImageView imgDelete;

    public GruppoContainerComponent()
    {
        //super();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(
            "fxml/controls/GruppoContainerComponent.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.setClassLoader(getClass().getClassLoader());

        try {
            fxmlLoader.load();
        } catch (IOException exception)
        {
            throw new RuntimeException(exception);
        }

        listBambini = new OrderedFilteredList<>();
        filterBambini = new FilterComponent<>(listBambini.predicateProperty());

        filterBambini.addFilterField(txtFilterBambini.textProperty(), p -> Filters.filterPersona(p, txtFilterBambini.getText()));
        setupTableSorvegliante();
        setupTableBambini();
        bind(null, null);
    }

    public GruppoContainerComponent(Gruppo gruppo)
    {
        this();
        bind(gruppo, null);
    }

    public GruppoContainerComponent(Gruppo gruppo, MezzoDiTrasporto mezzoDiTrasporto)
    {
        this();
        bind(gruppo, mezzoDiTrasporto);
    }

    /**
     * Associa all'attuale componente grafico un gruppo e un mezzo di trasporto (entrambi nullable)
     * ed aggiorna l'interfaccia grafica
     * @param gruppo Gruppo da associare al controllo
     * @param mezzoDiTrasporto MezzoDiTrasporto da associare al controllo
     */
    public void bind(Gruppo gruppo, MezzoDiTrasporto mezzoDiTrasporto)
    {
        this.linkedGruppo = gruppo;
        this.linkedMezzo = mezzoDiTrasporto;

        if(this.linkedGruppo == null)
            lblGroupName.setText("Crea Gruppo");
        else
            lblGroupName.setText("Gruppo " + linkedGruppo.getID());

        if(linkedGruppo != null)
        {
            if(linkedGruppo.getSorvergliante() != null)
                tableSorvegliante.getItems().add(linkedGruppo.getSorvergliante());

            listBambini.updateDataSet(FXCollections.observableArrayList(linkedGruppo.getBambini()));
        }

        if(linkedMezzo != null)
            lblGroupName.setText(lblGroupName.getText() + " - Autobus " + linkedMezzo.getTarga());
    }

    /**
     * @return Gruppo che rappresenta lo stato attuale dell'interfaccia grafica del controllo
     */
    public Gruppo getCurrentGruppoRappresentation()
    {
        Gruppo gruppo = new Gruppo();
        if(linkedGruppo != null)
            gruppo.unsafeSetID(linkedGruppo.getID());

        if(tableSorvegliante.getItems().size() > 0)
            gruppo.setSorvergliante(tableSorvegliante.getItems().get(0));

        for(Bambino bambino : tableBambini.getItems())
            gruppo.unsafeAddBambino(bambino);

        return gruppo;
    }

    /**
     * Abilita/Disabilita la modifica dei parametri del gruppo tramite Drag&Drop
     * @param enabled Indica se la modifica è consentita
     */
    public void setDragEnabled(boolean enabled)
    {
        this.tableSorvegliante.dragForClass(enabled ? Addetto.class : null);
        this.tableBambini.dragForClass(enabled ? Bambino.class : null);
    }

    private void setupTableSorvegliante()
    {
        TableColumn<Addetto, String> cMatricola = new TableColumn<>("ID");
        TableColumn<Addetto, String> cNome = new TableColumn<>("Nome");
        TableColumn<Addetto, String> cCognome = new TableColumn<>("Cognome");
        TableColumn<Addetto, String> cTelefoni = new TableColumn<>("Telefoni");

        cMatricola.setCellValueFactory(p -> new ReadOnlyStringWrapper(String.valueOf(p.getValue().getID())));
        cNome.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getNome()));
        cCognome.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getCognome()));
        cTelefoni.setCellValueFactory(p -> new ReadOnlyStringWrapper(EntitiesHelper.getTelefoniStringFromIterable(p.getValue().getTelefoni())));

        cMatricola.setMinWidth(50);
        cMatricola.setMaxWidth(50);
        cMatricola.setPrefWidth(50);

        if(tableSorvegliante != null)
            tableSorvegliante.getColumns().addAll(cMatricola, cNome, cCognome, cTelefoni);
    }

    private void setupTableBambini()
    {
        TableColumn<Bambino, String> cMatricola = new TableColumn<>("ID");
        TableColumn<Bambino, String> cNome = new TableColumn<>("Nome");
        TableColumn<Bambino, String> cCognome = new TableColumn<>("Cognome");

        cMatricola.setCellValueFactory(p -> new ReadOnlyStringWrapper(String.valueOf(p.getValue().getID())));
        cNome.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getNome()));
        cCognome.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getCognome()));

        cMatricola.setMinWidth(50);
        cMatricola.setMaxWidth(50);
        cMatricola.setPrefWidth(50);

        if(tableBambini != null)
        {
            tableBambini.getColumns().addAll(cMatricola, cNome, cCognome);
            listBambini.comparatorProperty().bind(tableBambini.comparatorProperty());
            tableBambini.setItems(listBambini.list());


            tableBambini.setOnElementDropped((element, source, target) -> {
                if(!listBambini.unfilteredContains(element))
                {
                    listBambini.add(element);
                    if(onBambinoAggiunto != null)
                        onBambinoAggiunto.execute(element, source, target);
                }
            });

            tableBambini.setOnElementDeleted(((element, source, target) -> {
                listBambini.remove(element);
                if(onBambinoRimosso != null)
                    onBambinoRimosso.execute(element, source, target);
            }));
        }
    }

    public StringProperty labelTextProperty() {
        return lblGroupName.textProperty();
    }

    public String getLabelText() {
        return labelTextProperty().get();
    }

    public void setLabelText(String text) {
        labelTextProperty().set(text);
    }

    /**
     * Permette di aggiungere nuove colonne alla tabella rappresentate i bambini
     * @param column Colonna da aggiungere
     * @param <T> Tipo di parametro della colonna inserita
     */
    public <T> void addBambiniColumn(TableColumn<Bambino, T> column)
    {
        if(tableBambini != null)
            tableBambini.getColumns().add(column);
    }

    /**
     * Rimuove un bambino dall'attuale rappresentazione del gruppo (non solleva alcun callback)
     * @param bambino Bambino da rimuovere
     */
    public void removeBambino(Bambino bambino)
    {
        this.listBambini.remove(bambino);
    }

    /**
     * Abilita la possibilità di avere un bottone di cancellazione in testa al controllo
     * @param onDeleteClicked Callback chiamato alla pressione del bottone
     */
    public void setOnDeleteClicked(EventHandler<? super MouseEvent> onDeleteClicked)
    {
        if(imgDelete != null)
        {
            imgDelete.setVisible(onDeleteClicked != null);
            imgDelete.setOnMouseClicked(onDeleteClicked);
        }
    }

    /**
     * Imposta un callback in ascolto sull'aggiunta di un bambino al gruppo
     * @param bambinoAggiunto Bambino aggiunto al gruppo
     */
    public void setOnBambinoAggiunto(IElementDragDropEvent<Bambino> bambinoAggiunto)
    {
        this.onBambinoAggiunto = bambinoAggiunto;
    }

    /**
     * Imposta un callback in ascolto sulla rimozione di un bambino dal gruppo
     * @param bambinoRimosso Bambino rimosso dal gruppo
     */
    public void setOnBambinoRimosso(IElementDragDropEvent<Bambino> bambinoRimosso)
    {
        this.onBambinoRimosso = bambinoRimosso;
    }
}
