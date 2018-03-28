package com.polimi.childcare.server.networking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;

public class NetworkManager
{
    private static NetworkManager _instance;
    public static NetworkManager getInstance() { return _instance != null ? _instance : (_instance = new NetworkManager()); }

    private ArrayList<IServerNetworkInterface> interfaces;
    private boolean _isRunning = false;

    //Avvia tutte le interfacce di rete
    public void listen(String address, Dictionary<IServerNetworkInterface, Integer> networkMapping) throws IOException
    {
        IServerNetworkInterface networkInterface = null;
        while((networkInterface = networkMapping.keys().nextElement()) != null)
        {
            this.interfaces.add(networkInterface);
            networkInterface.listen(address, networkMapping.get(networkInterface));
        }
    }

    //Ferma tutte le interfacce di rete
    public void stop()
    {
        for(IServerNetworkInterface networkInterface : interfaces)
            networkInterface.stop();
    }
}
