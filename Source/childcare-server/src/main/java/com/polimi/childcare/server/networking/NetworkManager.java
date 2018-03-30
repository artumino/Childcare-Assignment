package com.polimi.childcare.server.networking;

import com.polimi.childcare.shared.networking.requests.BaseRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

import javax.persistence.Transient;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;

public class NetworkManager implements IRequestHandler,Serializable
{
    private static NetworkManager _instance;
    public static NetworkManager getInstance() { return _instance != null ? _instance : (_instance = new NetworkManager()); }

    private transient ArrayList<IServerNetworkInterface> interfaces;
    private HashMap<Class<? extends BaseRequest>, IRequestHandler> requestHandlersMap;
    private boolean _isRunning = false;

    NetworkManager()
    {
        this.interfaces = new ArrayList<>();
        this.requestHandlersMap = new HashMap<>();
    }

    //Avvia tutte le interfacce di rete
    public void listen(String address, HashMap<IServerNetworkInterface, Integer> networkMapping) throws IOException
    {
        for(IServerNetworkInterface networkInterface : networkMapping.keySet())
        {
            this.interfaces.add(networkInterface);
            networkInterface.listen(address, networkMapping.get(networkInterface), this);
        }
    }

    //Ferma tutte le interfacce di rete
    public void stop()
    {
        for(IServerNetworkInterface networkInterface : interfaces)
            networkInterface.stop();
    }

    //Registra un nuovo gestore di richieste
    //TODO: JML?
    public boolean addRequestHandler(Class<? extends BaseRequest> requestClass, IRequestHandler handler)
    {
        if(requestClass != null &&
                handler != null &&
                !requestHandlersMap.containsKey(requestClass)) {
            requestHandlersMap.put(requestClass, handler);
            return true;
        }
        return false;
    }

    //Rimuove un handler già inserito
    public void removeRequestHandler(Class<? extends BaseRequest> requestClass)
    {
        if(requestClass != null &&
                requestHandlersMap.containsKey(requestClass)) {
            requestHandlersMap.remove(requestClass);
        }
    }

    //Gestisce tutti i messaggi in arrivo da tutte le interfacce che ereditano BaseServerNetworkInterface
    //Accede alla mappa degli handler per ogni classe di richiesta
    @Override
    public BaseResponse processRequest(BaseRequest request)
    {
        //Cerca un handler registrato per gestire il tipo di richiesta ricevuta
        IRequestHandler assignedHandler = requestHandlersMap.get(request.getClass());
        if (assignedHandler != null)
            return assignedHandler.processRequest(request);

        return new BadRequestResponse(); //Default 403 BAD REQUEST
    }
}
