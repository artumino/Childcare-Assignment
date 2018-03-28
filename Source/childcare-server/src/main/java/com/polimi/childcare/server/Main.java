package com.polimi.childcare.server;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.networking.sockets.SocketInterfaceServer;
import com.polimi.childcare.server.networking.IServerNetworkInterface;

import java.io.IOException;

public class Main
{
    public static void main(String... args)
    {
        System.out.println("Hello Server...");

        IServerNetworkInterface networkInterface = new SocketInterfaceServer();
        try {
            networkInterface.listen("localhost", 55403);
        } catch (IOException e)
        {
            System.out.println("Errore durante l'avvio dell'interfaccia di rete");
            e.printStackTrace();
        }

        System.out.println("Setting up server...");
        DatabaseSession.getInstance().setUp();
        System.out.println("Server setup complete " + DatabaseSession.getInstance().getCurrentConnectionURL());
        DatabaseSession.getInstance().close();

        networkInterface.stop();
    }
}
