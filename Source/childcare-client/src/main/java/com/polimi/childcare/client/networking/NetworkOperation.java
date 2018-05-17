package com.polimi.childcare.client.networking;

import com.polimi.childcare.shared.networking.requests.BaseRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import javafx.application.Platform;

public class NetworkOperation
{
    public interface INetworkOperationCallback
    {
        void OnResult(BaseResponse response);
    }

    private final Object callbackMutex = new Object();
    private INetworkOperationCallback callback;
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
    public void executeCallback(final BaseResponse response)
    {
        synchronized (callbackMutex)
        {
            if(callback != null)
            {
                if(runOnUiThread)
                    Platform.runLater(() -> callback.OnResult(response));
                else
                    callback.OnResult(response);
            }
        }
    }

    /**
     * Imposta un nuovo callback da chiamare una volta ricevuta la risposta alla richiesta(immutabile)
     * @param callback null per disabilitare il callback, INetworkOperationCallback per un nuovo callback
     */
    public void setCallback(INetworkOperationCallback callback)
    {
        synchronized (callbackMutex)
        {
            this.callback = callback;
        }
    }

    /**
     * @return Richiesta da effettuare al server
     */
    public BaseRequest getRequest()
    {
        return request;
    }
}
