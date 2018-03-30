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

/**
 * Singleton per la gestione delle interfacce di rete e le comunicazioni che avvengono tramite esse
 */
public class NetworkManager implements IRequestHandler,Serializable
{
    //Singleton
    private static NetworkManager _instance;

    /**
     * @return Istanza del singleton attuale
     */
    public static NetworkManager getInstance() { return _instance != null ? _instance : (_instance = new NetworkManager()); }

    private transient ArrayList<IServerNetworkInterface> interfaces;
    private HashMap<Class<? extends BaseRequest>, IRequestHandler> requestHandlersMap;
    private boolean _isRunning = false;

    NetworkManager()
    {
        this.interfaces = new ArrayList<>();
        this.requestHandlersMap = new HashMap<>();
    }

    /**
     * Avvia tutte le interfacce di rete
     * @param address Indirizzo per l'ascolto di nuovi client
     * @param networkMapping Mappa contenente le informazioni di inizializzazione su ogni interfaccia di rete driverRete:{@link IServerNetworkInterface}, porta:{@link Integer}
     * @throws IOException In caso di impossibilità nell'apertura di una interfaccia
     */
    public void listen(String address, HashMap<IServerNetworkInterface, Integer> networkMapping) throws IOException
    {
        for(IServerNetworkInterface networkInterface : networkMapping.keySet())
        {
            this.interfaces.add(networkInterface);
            networkInterface.listen(address, networkMapping.get(networkInterface), this);
        }
    }

    /**
     * Ferma tutte le interfacce di rete
     */
    public void stop()
    {
        for(IServerNetworkInterface networkInterface : interfaces)
            networkInterface.stop();
    }

    /**
     * Registra un nuovo gestore di richieste.
     * N.B. Può essere registrato solo un handler per tipo di richiesta.
     * @param requestClass Tipo di richiesta da gestire
     * @param handler Strategia per la gestione della richiesta
     * @return true se l'inserimento è avvenuto senza problemi, false in caso vi sia già un handler registrato per questa richiesta (o in caso almeno uno dei parametri sia null)
     */
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

    /**
     * Rimuove un handler già inserito
     * @param requestClass Tipo da richiesta per cui si vuole rimuovere l'handler
     */
    public void removeRequestHandler(Class<? extends BaseRequest> requestClass)
    {
        if(requestClass != null &&
                requestHandlersMap.containsKey(requestClass)) {
            requestHandlersMap.remove(requestClass);
        }
    }

    /**
     * Gestisce tutti i messaggi in arrivo dalle interfacce per cui questo {@link IRequestHandler<BaseRequest>} è stato registrato come handler di default delle richieste
     * Accede alla mappa degli handler per ogni classe di richiesta
     * @param request Richiesta di tipo T che estende {@link BaseRequest}
     * @return Risposta che verrà inoltrata al client (passando per i driver di rete opportuni)
     */
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
