package com.polimi.childcare.server;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.handlers.BambiniRequestHandler;
import com.polimi.childcare.server.networking.NetworkManager;
import com.polimi.childcare.server.networking.rmi.RMIInterfaceServer;
import com.polimi.childcare.server.networking.sockets.SocketInterfaceServer;
import com.polimi.childcare.server.networking.IServerNetworkInterface;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredBambiniRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Main
{
    public static void main(String... args)
    {
        System.out.println("Hello Server...");

        NetworkManager networkManager = NetworkManager.getInstance();
        try
        {
            //Inizializza due interfacce di rete
            //Socket: localhost:55403
            //RMI: localhost:55404
            networkManager.listen("localhost", new HashMap<IServerNetworkInterface, Integer>(2)
            {{
                put(new SocketInterfaceServer(), 55403);
                put(new RMIInterfaceServer(), 55404);
            }});
        } catch (IOException e)
        {
            System.out.println("Errore durante l'avvio dell'interfaccia di rete");
            e.printStackTrace();
        }

        System.out.println("Setting up server...");
        DatabaseSession.getInstance().setUp();
        System.out.println("Server setup complete " + DatabaseSession.getInstance().getCurrentConnectionURL());

        //Aggiunge handler al network manager
        NetworkManager.getInstance().addRequestHandler(FilteredBambiniRequest.class, new BambiniRequestHandler());

        String command;
        Scanner scanner = new Scanner(System.in);
        while((command = scanner.nextLine()) != null && !command.equals("quit"))
        {
            //DO NOTHING
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        DatabaseSession.getInstance().close();
        networkManager.stop();
    }
}
