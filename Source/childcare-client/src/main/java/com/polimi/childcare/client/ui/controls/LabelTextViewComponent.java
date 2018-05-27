package com.polimi.childcare.client.ui.controls;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class LabelTextViewComponent extends GridPane
{
    @FXML private Label txtLabel;
    @FXML private TextField txtTextField;


    public LabelTextViewComponent()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(
                "fxml/controls/LabelTextViewComponent.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }

    public DoubleProperty labelPercentWidthProperty() { return getColumnConstraints().get(0).percentWidthProperty(); }

    public double getLabelPercentWidth()
    {
        return labelPercentWidthProperty().get();
    }

    public void setLabelPercentWidth(double percentWidth)
    {
        labelPercentWidthProperty().set(percentWidth);
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
        return txtTextField.textProperty();
    }

    public String getTextFieldText()
    {
        return textFieldTextProperty().get();
    }

    public void setTextFieldText(String text)
    {
        textFieldTextProperty().set(text);
    }

    public StringProperty textFieldPromptProperty() {
        return txtTextField.promptTextProperty();
    }

    public String getTextFieldPrompt()
    {
        return textFieldPromptProperty().get();
    }

    public void setTextFieldPrompt(String text)
    {
        textFieldPromptProperty().set(text);
    }
}
