package com.polimi.childcare.client.shared.networking;

import com.polimi.childcare.shared.networking.requests.BaseRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import javafx.application.Platform;

import java.io.Serializable;

public class NetworkOperation implements Serializable
{
    public interface INetworkOperationCallback
    {
        void OnResult(BaseResponse response);
    }

    private transient INetworkOperationCallback callback;
    private BaseRequest request;
    private boolean runOnUiThread;

    /**
     * Crea una nuova richiesta di operazione di rete, pronta per essere sottomessa alla lista delle operazioni di rete
     * @param request Richiesta da effettuare al server, NullPointerExcepion in caso request sia null
     * @param callback Callback chiamato una volta ricevuta la risposta
     * @param runOnUiThread true per eseguire tutto il callback sul thread di JavaFX, false esegue direttamente il callback
     *                      nel thread di rete (il programmatore dovrà preoccuparsi di gestire le operazioni sull'interfaccia grafica opportunamente)
     */
    public NetworkOperation(BaseRequest request, INetworkOperationCallback callback, boolean runOnUiThread)
    {
        initialize(request, callback, runOnUiThread);
    }

    public NetworkOperation(BaseRequest request, INetworkOperationCallback callback)
    {
        initialize(request, callback, false);
    }

    public  NetworkOperation(BaseRequest request)
    {
        initialize(request, null, false);
    }

    private void initialize(BaseRequest request, INetworkOperationCallback callback, boolean runOnUiThread)
    {
        if(request == null)
            throw new NullPointerException();

        this.request = request;
        this.callback = callback;
        this.runOnUiThread = runOnUiThread;
    }

    /**
     * Chiama il callback come impostato alla creazione dell'oggetto (se questo è presente)
     * @param response Risposta ricevuta dal server
     */
    public synchronized void executeCallback(final BaseResponse response)
    {
        if(callback != null)
        {
            if(runOnUiThread)
                Platform.runLater(() -> callback.OnResult(response));
            else
                callback.OnResult(response);
        }
    }

    /**
     * Imposta un nuovo callback da chiamare una volta ricevuta la risposta alla richiesta(immutabile)
     * @param callback null per disabilitare il callback, INetworkOperationCallback per un nuovo callback
     */
    public synchronized void setCallback(INetworkOperationCallback callback)
    {
        this.callback = callback;
    }

    /**
     * Imposta se il callback verrà eseguito o meno sull'interfaccia grafica (N.B. vale solo per JavaFX, Android usa una tecnica differente)
     * @param runOnUiThread true il callback verrà eseguito sul thread di JavaFX, false il callback verrà eseguito sul thread di rete
     */
    public void setRunOnUiThread(boolean runOnUiThread)
    {
        this.runOnUiThread = runOnUiThread;
    }

    /**
     * @return Richiesta da effettuare al server
     */
    public BaseRequest getRequest()
    {
        return request;
    }
}
