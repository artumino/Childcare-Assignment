package com.polimi.childcare.client.android;

import android.app.Application;
import com.polimi.childcare.client.shared.networking.ClientNetworkManager;
import com.polimi.childcare.client.shared.networking.sockets.SocketInterfaceClient;

public class ChildcareApp extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        //Inizializza i componenti di rete, ho solo una interfaccia quindi non ho problemi
        ClientNetworkManager.getInstance().SetInterface(new SocketInterfaceClient());

        //Proseguo con l'avvio dell'app
    }
}
