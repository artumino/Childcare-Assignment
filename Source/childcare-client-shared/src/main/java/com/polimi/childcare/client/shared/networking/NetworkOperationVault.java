package com.polimi.childcare.client.shared.networking;

import com.polimi.childcare.shared.networking.requests.BaseRequest;

import java.util.HashMap;

/**
 * Classe che permette all'interfaccia grafica di tenere traccia delle proprie richieste effettuate ma non ancora soddisfatte
 */
public class NetworkOperationVault
{
    private HashMap<Class<? extends BaseRequest>, NetworkOperation> runningNetworkOperationMap;

    public NetworkOperationVault()
    {
        runningNetworkOperationMap = new HashMap<>();
    }

    public synchronized void submitOperation(NetworkOperation operation)
    {
        if(operation == null || operation.getRequest() == null)
            return;

        //Se già presente ripubblico la nuova richiesta la richiesta
        this.abortOperation(operation.getRequest().getClass());

        runningNetworkOperationMap.put(operation.getRequest().getClass(), operation);
        ClientNetworkManager.getInstance().submitOperation(operation);
    }

    public synchronized void operationDone(Class<? extends BaseRequest> request)
    {
        runningNetworkOperationMap.remove(request);
    }

    public synchronized boolean anyRunningOperation() { return runningNetworkOperationMap.keySet().size() > 0; }
    public synchronized boolean operationRunning(Class<? extends BaseRequest> request) { return runningNetworkOperationMap.containsKey(request); }

    public synchronized void abortOperation(Class<? extends BaseRequest> request)
    {
        if(runningNetworkOperationMap.containsKey(request))
        {
            ClientNetworkManager.getInstance().abortOperation(runningNetworkOperationMap.get(request));
            runningNetworkOperationMap.remove(request);
        }
    }

    public synchronized void abortAllOperations()
    {
        for(NetworkOperation operation : runningNetworkOperationMap.values())
            ClientNetworkManager.getInstance().abortOperation(operation);

        runningNetworkOperationMap.clear();
    }
}
