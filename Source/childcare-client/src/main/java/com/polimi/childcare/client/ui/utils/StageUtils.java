package com.polimi.childcare.client.ui.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import jdk.nashorn.internal.runtime.options.Option;

import java.util.Optional;

public class StageUtils
{
    public static void ShowAlert(Alert.AlertType type, String contentText)
    {
        Alert alert = new Alert(type, contentText);
        alert.showAndWait();
    }

    public static Optional<ButtonType> ShowAlertWithButtons(Alert.AlertType type, String contentText, ButtonType... buttons)
    {
        Alert alert = new Alert(type, contentText, buttons);
        return alert.showAndWait();
    }
}
