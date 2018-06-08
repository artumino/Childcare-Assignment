package com.polimi.childcare.client.ui.controls;

import com.polimi.childcare.shared.entities.Menu;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class LabelRecurrenceComponent extends GridPane {
    @FXML private Label txtLabel;
    @FXML private CheckBox cbLun;
    @FXML private CheckBox cbMar;
    @FXML private CheckBox cbMer;
    @FXML private CheckBox cbGio;
    @FXML private CheckBox cbVen;
    @FXML private CheckBox cbSab;
    @FXML private CheckBox cbDom;

    private SimpleIntegerProperty recurrenceMask;

    public LabelRecurrenceComponent() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(
                "fxml/controls/LabelRecurrenceComponent.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        recurrenceMask = new SimpleIntegerProperty(0x00);

        recurrenceMask.addListener((observable, oldValue, newValue) ->
        {
            if(!oldValue.equals(newValue))
            {
                cbLun.setSelected((this.recurrenceMask.get() & Menu.DayOfWeekFlag.Lun.getFlag()) != 0);
                cbMar.setSelected((this.recurrenceMask.get() & Menu.DayOfWeekFlag.Mar.getFlag()) != 0);
                cbMer.setSelected((this.recurrenceMask.get() & Menu.DayOfWeekFlag.Mer.getFlag()) != 0);
                cbGio.setSelected((this.recurrenceMask.get() & Menu.DayOfWeekFlag.Gio.getFlag()) != 0);
                cbVen.setSelected((this.recurrenceMask.get() & Menu.DayOfWeekFlag.Ven.getFlag()) != 0);
                cbSab.setSelected((this.recurrenceMask.get() & Menu.DayOfWeekFlag.Sab.getFlag()) != 0);
                cbDom.setSelected((this.recurrenceMask.get() & Menu.DayOfWeekFlag.Dom.getFlag()) != 0);
            }
        });

        //Casino per far si che l'oggetto sia un POJO (SceneBuilder non supporta oggetti più complessi)
        cbLun.selectedProperty().addListener((observable, oldValue, newValue) ->{
            Menu.DayOfWeekFlag flag = Menu.DayOfWeekFlag.Lun;
            if(newValue)
                this.recurrenceMask.set( this.recurrenceMask.get() | flag.getFlag());
            else if((this.recurrenceMask.get() & flag.getFlag()) != 0)
                this.recurrenceMask.set(this.recurrenceMask.get() - flag.getFlag());
        });

        cbMar.selectedProperty().addListener((observable, oldValue, newValue) ->{
            Menu.DayOfWeekFlag flag = Menu.DayOfWeekFlag.Mar;
            if(newValue)
                this.recurrenceMask.set( this.recurrenceMask.get() | flag.getFlag());
            else if((this.recurrenceMask.get() & flag.getFlag()) != 0)
                this.recurrenceMask.set(this.recurrenceMask.get() - flag.getFlag());
        });

        cbMer.selectedProperty().addListener((observable, oldValue, newValue) ->{
            Menu.DayOfWeekFlag flag = Menu.DayOfWeekFlag.Mer;
            if(newValue)
                this.recurrenceMask.set( this.recurrenceMask.get() | flag.getFlag());
            else if((this.recurrenceMask.get() & flag.getFlag()) != 0)
                this.recurrenceMask.set(this.recurrenceMask.get() - flag.getFlag());
        });

        cbGio.selectedProperty().addListener((observable, oldValue, newValue) ->{
            Menu.DayOfWeekFlag flag = Menu.DayOfWeekFlag.Gio;
            if(newValue)
                this.recurrenceMask.set( this.recurrenceMask.get() | flag.getFlag());
            else if((this.recurrenceMask.get() & flag.getFlag()) != 0)
                this.recurrenceMask.set(this.recurrenceMask.get() - flag.getFlag());
        });

        cbVen.selectedProperty().addListener((observable, oldValue, newValue) ->{
            Menu.DayOfWeekFlag flag = Menu.DayOfWeekFlag.Ven;
            if(newValue)
                this.recurrenceMask.set( this.recurrenceMask.get() | flag.getFlag());
            else if((this.recurrenceMask.get() & flag.getFlag()) != 0)
                this.recurrenceMask.set(this.recurrenceMask.get() - flag.getFlag());
        });

        cbSab.selectedProperty().addListener((observable, oldValue, newValue) ->{
            Menu.DayOfWeekFlag flag = Menu.DayOfWeekFlag.Sab;
            if(newValue)
                this.recurrenceMask.set( this.recurrenceMask.get() | flag.getFlag());
            else if((this.recurrenceMask.get() & flag.getFlag()) != 0)
                this.recurrenceMask.set(this.recurrenceMask.get() - flag.getFlag());
        });

        cbDom.selectedProperty().addListener((observable, oldValue, newValue) ->{
            Menu.DayOfWeekFlag flag = Menu.DayOfWeekFlag.Dom;
            if(newValue)
                this.recurrenceMask.set( this.recurrenceMask.get() | flag.getFlag());
            else if((this.recurrenceMask.get() & flag.getFlag()) != 0)
                this.recurrenceMask.set(this.recurrenceMask.get() - flag.getFlag());
        });
    }

    public DoubleProperty labelPercentWidthProperty() {
        return getColumnConstraints().get(0).percentWidthProperty();
    }

    public double getLabelPercentWidth() {
        return labelPercentWidthProperty().get();
    }

    public void setLabelPercentWidth(double percentWidth) {
        labelPercentWidthProperty().set(percentWidth);
    }

    public StringProperty labelTextProperty() {
        return txtLabel.textProperty();
    }

    public String getLabelText() {
        return labelTextProperty().get();
    }

    public void setLabelText(String text) {
        labelTextProperty().set(text);
    }

    public IntegerProperty recurrenceProperty() {
        return recurrenceMask;
    }

    public int getRecurrence() {
        return recurrenceProperty().get();
    }

    public void setRecurrence(int recurrence) {
        recurrenceProperty().set(recurrence);
    }
}