package com.polimi.childcare.client.ui.controls;

import com.polimi.childcare.shared.entities.Gruppo;
import com.polimi.childcare.shared.entities.MezzoDiTrasporto;
import com.polimi.childcare.shared.serialization.SerializationUtils;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.Base64;

public class PianoViaggiComponent extends HBox
{
    private Gruppo linkedGruppo;
    private MezzoDiTrasporto linkedMezzo;

    @FXML private Label txtLabel;
    @FXML private Label txtTransport;
    @FXML private ImageView imgTransport;
    @FXML private HBox hboxMezzo;


    public PianoViaggiComponent()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(
                "fxml/controls/PianoViaggiComponent.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        setOnDragDetected(event -> event.consume());

        setOnDragExited(event -> {
            if(event.getGestureSource() == this)
                bindMezzo(null);
        });

        setOnDragOver(event -> {
            if (event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasString())
            {
                String dataString = db.getString();
                byte[] data = Base64.getDecoder().decode(dataString);
                MezzoDiTrasporto element = SerializationUtils.deserializeByteArray(data, MezzoDiTrasporto.class);
                if (element != null)
                {
                    bindMezzo(element);
                    event.setDropCompleted(true);
                }
            }
            event.consume();
        });
    }

    public void bindGruppo(Gruppo gruppo)
    {
        this.linkedGruppo = gruppo;
        updateContols();
    }

    public void bindMezzo(MezzoDiTrasporto mezzoDiTrasporto)
    {
        this.linkedMezzo = mezzoDiTrasporto;
        updateContols();
    }

    public Gruppo getLinkedGruppo() {
        return linkedGruppo;
    }

    public MezzoDiTrasporto getLinkedMezzo() {
        return linkedMezzo;
    }

    private void updateContols()
    {
        if(linkedGruppo == null)
            txtLabel.setText("Gruppo");
        else
            txtLabel.setText("Gruppo " + linkedGruppo.getID());

        if(linkedMezzo == null)
        {
            txtTransport.setText("Piedi");
            try {
                imgTransport.setImage(new Image(getClass().getClassLoader().getResource("fxml/images/baseline_directions_walk_black_18dp.png").openStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            txtTransport.setText("Mezzo " + linkedMezzo.getTarga() + "\nID: " + linkedMezzo.getNumeroIdentificativo());

            try {
                imgTransport.setImage(new Image(getClass().getClassLoader().getResource("fxml/images/baseline_directions_bus_black_18dp.png").openStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public StringProperty labelTextProperty() {
        return txtLabel.textProperty();
    }

    public String getLabelText()
    {
        return labelTextProperty().get();
    }

    public void setLabelText(String text)
    {
        labelTextProperty().set(text);
    }

    public StringProperty textFieldTextProperty() {
        return txtTransport.textProperty();
    }

    public String getTextFieldText()
    {
        return textFieldTextProperty().get();
    }

    public void setTextFieldText(String text)
    {
        textFieldTextProperty().set(text);
    }
}
