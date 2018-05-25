package com.polimi.childcare.server;

import com.polimi.childcare.server.database.DatabaseDemo;
import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.handlers.entities.getters.FilteredRegistroPresenzeRequestHandler;
import com.polimi.childcare.server.networking.NetworkManager;
import com.polimi.childcare.server.networking.rmi.RMIInterfaceServer;
import com.polimi.childcare.server.networking.sockets.SocketInterfaceServer;
import com.polimi.childcare.server.networking.IServerNetworkInterface;
import com.polimi.childcare.shared.networking.requests.filtered.*;

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
        NetworkManager.getInstance().addRequestHandler(FilteredAddettoRequest.class, new com.polimi.childcare.server.handlers.entities.getters.FilteredAddettiRequestHandler());
        NetworkManager.getInstance().addRequestHandler(FilteredBambiniRequest.class, new com.polimi.childcare.server.handlers.entities.getters.FilteredBambiniRequestHandler());
        NetworkManager.getInstance().addRequestHandler(FilteredContattoRequest.class, new com.polimi.childcare.server.handlers.entities.getters.FilteredContattoRequestHandler());
        NetworkManager.getInstance().addRequestHandler(FilteredDiagnosiRequest.class, new com.polimi.childcare.server.handlers.entities.getters.FilteredDiagnosiRequestHandler());
        NetworkManager.getInstance().addRequestHandler(FilteredFornitoriRequest.class, new com.polimi.childcare.server.handlers.entities.getters.FilteredFornitoreRequestHandler());
        NetworkManager.getInstance().addRequestHandler(FilteredGenitoriRequest.class, new com.polimi.childcare.server.handlers.entities.getters.FilteredGenitoreRequestHandler());
        NetworkManager.getInstance().addRequestHandler(FilteredGitaRequest.class, new com.polimi.childcare.server.handlers.entities.getters.FilteredGitaRequestHandler());
        NetworkManager.getInstance().addRequestHandler(FilteredGruppoRequest.class, new com.polimi.childcare.server.handlers.entities.getters.FilteredGruppoRequestHandler());
        NetworkManager.getInstance().addRequestHandler(FilteredMenuRequest.class, new com.polimi.childcare.server.handlers.entities.getters.FilteredMenuRequestHandler());
        NetworkManager.getInstance().addRequestHandler(FilteredMezzoDiTrasportoRequest.class, new com.polimi.childcare.server.handlers.entities.getters.FilteredMezzoDiTrasportoRequestHandler());
        NetworkManager.getInstance().addRequestHandler(FilteredNumeroTelefonoRequest.class, new com.polimi.childcare.server.handlers.entities.getters.FilteredNumeroTelefonoRequestHandler());
        NetworkManager.getInstance().addRequestHandler(FilteredPastoRequest.class, new com.polimi.childcare.server.handlers.entities.getters.FilteredPastiRequestHandler());
        NetworkManager.getInstance().addRequestHandler(FilteredPediatraRequest.class, new com.polimi.childcare.server.handlers.entities.getters.FilteredPediatraRequestHandler());
        NetworkManager.getInstance().addRequestHandler(FilteredPersonaRequest.class, new com.polimi.childcare.server.handlers.entities.getters.FilteredPersonaRequestHandler());
        NetworkManager.getInstance().addRequestHandler(FilteredPianoViaggiRequest.class, new com.polimi.childcare.server.handlers.entities.getters.FilteredPianoViaggiRequestHandler());
        NetworkManager.getInstance().addRequestHandler(FilteredQuantitaPastoRequest.class, new com.polimi.childcare.server.handlers.entities.getters.FilteredQuantitaPastoRequestHandler());
        NetworkManager.getInstance().addRequestHandler(FilteredReazioneAvversaRequest.class, new com.polimi.childcare.server.handlers.entities.getters.FilteredReazioneAvversaRequestHandler());
        NetworkManager.getInstance().addRequestHandler(FilteredRegistroPresenzeRequest.class, new FilteredRegistroPresenzeRequestHandler());

        String command;
        Scanner scanner = new Scanner(System.in);

        do
        {
            command = scanner.nextLine();
            //DO NOTHING
            /*try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

            String[] commands = command.split(" ");
            if(commands.length == 2 && commands[0].equals("demo"))
            {
                try
                {
                    DatabaseDemo.runDemoGeneration(Integer.parseInt(commands[1]));
                }
                catch (Throwable ex)
                {
                    ex.printStackTrace();
                }
            }
        } while(!command.equals("quit") && !command.equals("exit"));

        DatabaseSession.getInstance().close();
        networkManager.stop();
        System.exit(0);
    }
}
