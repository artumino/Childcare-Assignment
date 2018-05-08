package com.polimi.childcare.client.networking;

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
