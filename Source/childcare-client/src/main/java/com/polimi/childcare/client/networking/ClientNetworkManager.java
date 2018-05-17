package com.polimi.childcare.client.networking;

import com.polimi.childcare.shared.networking.responses.BaseResponse;

import java.util.concurrent.LinkedBlockingDeque;

public class ClientNetworkManager implements Runnable
{
    private static int RECONNECTION_TIMEOUT = 2000; //ms

    //Singleton
    private static ClientNetworkManager _instance;

    /**
     * @return Istanza del singleton attuale
     */
    public static ClientNetworkManager getInstance() { return _instance != null ? _instance : (_instance = new ClientNetworkManager()); }

    private String currentServerAddress;
    private int currentServerPort;

    private IClientNetworkInterface clientNetworkInterface;
    private boolean networkEnabled;
    private Thread networkThread;

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
    private LinkedBlockingDeque<NetworkOperation> networkOperationQueue;

    private ClientNetworkManager()
    {
        this.networkOperationQueue = new LinkedBlockingDeque<>();
        this.networkEnabled = true;
        this.networkThread = new Thread(this);
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
     * Ferma thread e libera le risorse, chiamare solo alla chiusura del programma
     */
    public void Dispose()
    {
        if(this.clientNetworkInterface != null && this.clientNetworkInterface.isConnected())
            this.clientNetworkInterface.close();

        this.networkEnabled = false;
        if(this.networkThread != null && this.networkThread.isAlive())
            this.networkThread.interrupt();
        this.networkThread = null;
    }

    /**
     * Prova ad aprire una connessione con il server
     * @param address Indirizzo del server
     * @param port Porta del server
     * @return true in caso di connessione riuscita (thread di rete attivo), false in caso di connessione non riuscita
     */
    public boolean tryConnect(String address, int port)
    {
        if(this.clientNetworkInterface != null) {

            //Se il thread di rete è ancora in esecuzione, non posso connettermi nuovamente
            if(this.clientNetworkInterface.isConnected())
                return false;

            try {
                this.currentServerAddress = address;
                this.currentServerPort = port;
                this.clientNetworkInterface.connect(address, port);

                //Controllo che il thread di gestione della rete sia attivo
                if(!this.networkThread.isAlive())
                    this.networkThread.start();

                return true;
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Aggiunge in testa alla coda delle operazioni di rete networkOperation
     * @param networkOperation Operazione di rete da effettuare
     */
    public void submitOperation(NetworkOperation networkOperation)
    {
        try {
            this.networkOperationQueue.putFirst(networkOperation);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * NetworkLoop, gestisce la coda delle operazioni di rete.
     * estrae un'operazione dalla coda (FIFO), prova ad eseguirla e ne valuta il successo.
     * In caso di insuccesso reinserisce l'operazione in fondo alla coda(pronta per essere riestratta).
     * In caso di errori di connessione, cerca di riconnetterni con un predeterminato tempo di retry
     */
    @Override
    public void run()
    {
        boolean lastConnected = false;
        while (networkEnabled)
        {
            if(this.clientNetworkInterface != null && this.clientNetworkInterface.isConnected())
            {
                lastConnected = true;
                NetworkOperation operation;
                try {
                    operation = networkOperationQueue.takeLast();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    continue;
                }

                //null in caso di errori di connessione
                BaseResponse response = this.clientNetworkInterface.sendMessage(operation.getRequest());


                if(response == null) {
                    //In caso di errore di connessione ripristino la richiesta nella lista delle richieste
                    try {
                        networkOperationQueue.putLast(operation);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        continue;
                    }
                }
                else
                    //In caso di operazione andata a buon fine, eseguo il callback
                    operation.executeCallback(response);
            }
            else if(this.clientNetworkInterface != null && lastConnected) //Auto-Reconnect in caso di errori di rete
            {
                //Prova a riconnettersi ogni RECONNECTION_TIMEOUT ms
                if(!this.tryConnect(this.currentServerAddress, this.currentServerPort)) {
                    try {
                        Thread.sleep(RECONNECTION_TIMEOUT);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
