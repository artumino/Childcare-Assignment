package com.polimi.childcare.client.ui.utils;

import javafx.scene.control.Alert;

public class StageUtils
{
    public static void ShowAlert(Alert.AlertType type, String contentText)
    {
        Alert alert = new Alert(type, contentText);
        alert.showAndWait();
    }
}
