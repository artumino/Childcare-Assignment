package com.polimi.childcare.client.networking;

import com.polimi.childcare.shared.networking.requests.BaseRequest;

import java.util.ArrayList;
import java.util.List;

public class ClientNetworkManager
{
    //Singleton
    private static ClientNetworkManager _instance;

    /**
     * @return Istanza del singleton attuale
     */
    public static ClientNetworkManager getInstance() { return _instance != null ? _instance : (_instance = new ClientNetworkManager()); }

    private IClientNetworkInterface clientNetworkInterface;

    /**
     * Cache delle richieste da mandare al server (in caso di disconnessione)
     * In fase di progettazione è stato deciso di avere un unico thread di gestione della coda delle richieste per permettere
     * una maggiore flessibilità nella gestione di errori di rete. Ogni operazione di rete prevede il submit di una richiesta
     * nella queue ed un rispettivo callback che verrà chiamato una volta che la richiesta è stata presa in carico ed elaborata
     * dal server.
     *
     * Una richiesta può anche essere inserita senza callback oppure può essere alterata per rimuovere il callback assegnato
     * (caso in cui una richiesta non viene presa in carico prima che il listener venga chiuso)
     */
    private ArrayList<NetworkOperation> networkOperationQueue;

    private ClientNetworkManager()
    {
        this.networkOperationQueue = new ArrayList<>(10);
    }

    /**
     *  Aggiorna l'interfaccia da utilizzare per le comunicazioni in rete, chiude eventuali connessioni sull'interfaccia precedente
     * @param iClientNetworkInterface nuova interfaccia da usare
     */
    public void SetInterface(IClientNetworkInterface iClientNetworkInterface)
    {
        if(this.clientNetworkInterface != null)
            this.clientNetworkInterface.close();

        this.clientNetworkInterface = iClientNetworkInterface;
    }

    /**
     * @return Ritorna l'interfaccia attualmente in uso per le comunicazioni di rete
     */
    public IClientNetworkInterface GetCurrentInterface()
    {
        return this.clientNetworkInterface;
    }
}
