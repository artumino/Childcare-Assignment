package com.polimi.childcare.client.ui.utils;

import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import jdk.nashorn.internal.runtime.options.Option;

import java.util.Optional;
import java.util.function.Predicate;

public class StageUtils
{
    /**
     * Esamina la risposta per eventuali errori, in caso di errore ritorna true e fa apparire un Alert opportuno
     * @param response Risposta da esaminare
     * @param defaultErrorMessage Messaggio di errore in caso la risposta non contenga una descrizione migliore
     * @param acceptedResponses Predicate che ritorna true in caso la risposta sia accettata (e quindi non è necessario notificare errori)
     * @return true in caso di errori, false in caso la risposta sia corretta
     */
    public static boolean HandleResponseError(BaseResponse response, String defaultErrorMessage, Predicate<BaseResponse> acceptedResponses)
    {
        if(!acceptedResponses.test(response))
        {
            if(response instanceof BadRequestResponse.BadRequestResponseWithMessage)
                ShowAlert(Alert.AlertType.ERROR, ((BadRequestResponse.BadRequestResponseWithMessage) response).getMessage());
            else
                ShowAlert(Alert.AlertType.ERROR, defaultErrorMessage);

            return true;
        }
        return false;
    }

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
