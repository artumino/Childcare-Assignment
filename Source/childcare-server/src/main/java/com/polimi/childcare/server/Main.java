package com.polimi.childcare.server;

import com.polimi.childcare.server.database.DatabaseDemo;
import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.handlers.entities.getters.*;
import com.polimi.childcare.server.handlers.entities.setters.*;
import com.polimi.childcare.server.handlers.entities.special.FilteredContattoOnlyRequestHandler;
import com.polimi.childcare.server.handlers.entities.special.SetPresenzaRequestHandler;
import com.polimi.childcare.server.networking.NetworkManager;
import com.polimi.childcare.server.networking.rmi.RMIInterfaceServer;
import com.polimi.childcare.server.networking.sockets.SocketInterfaceServer;
import com.polimi.childcare.server.networking.IServerNetworkInterface;
import com.polimi.childcare.shared.networking.requests.filtered.*;
import com.polimi.childcare.shared.networking.requests.setters.*;

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

        //region Getters
        NetworkManager.getInstance().addRequestHandler(FilteredAddettoRequest.class, new FilteredAddettiRequestHandler());
        NetworkManager.getInstance().addRequestHandler(FilteredBambiniRequest.class, new FilteredBambiniRequestHandler());
        NetworkManager.getInstance().addRequestHandler(FilteredContattoRequest.class, new FilteredContattoRequestHandler());
        NetworkManager.getInstance().addRequestHandler(FilteredDiagnosiRequest.class, new FilteredDiagnosiRequestHandler());
        NetworkManager.getInstance().addRequestHandler(FilteredFornitoriRequest.class, new FilteredFornitoreRequestHandler());
        NetworkManager.getInstance().addRequestHandler(FilteredGenitoriRequest.class, new FilteredGenitoreRequestHandler());
        NetworkManager.getInstance().addRequestHandler(FilteredGitaRequest.class, new FilteredGitaRequestHandler());
        NetworkManager.getInstance().addRequestHandler(FilteredGruppoRequest.class, new FilteredGruppoRequestHandler());
        NetworkManager.getInstance().addRequestHandler(FilteredMenuRequest.class, new FilteredMenuRequestHandler());
        NetworkManager.getInstance().addRequestHandler(FilteredMezzoDiTrasportoRequest.class, new FilteredMezzoDiTrasportoRequestHandler());
        NetworkManager.getInstance().addRequestHandler(FilteredNumeroTelefonoRequest.class, new FilteredNumeroTelefonoRequestHandler());
        NetworkManager.getInstance().addRequestHandler(FilteredPastoRequest.class, new FilteredPastiRequestHandler());
        NetworkManager.getInstance().addRequestHandler(FilteredPediatraRequest.class, new FilteredPediatraRequestHandler());
        NetworkManager.getInstance().addRequestHandler(FilteredPersonaRequest.class, new FilteredPersonaRequestHandler());
        NetworkManager.getInstance().addRequestHandler(FilteredPianoViaggiRequest.class, new FilteredPianoViaggiRequestHandler());
        NetworkManager.getInstance().addRequestHandler(FilteredQuantitaPastoRequest.class, new FilteredQuantitaPastoRequestHandler());
        NetworkManager.getInstance().addRequestHandler(FilteredReazioneAvversaRequest.class, new FilteredReazioneAvversaRequestHandler());
        NetworkManager.getInstance().addRequestHandler(FilteredRegistroPresenzeRequest.class, new FilteredRegistroPresenzeRequestHandler());
        NetworkManager.getInstance().addRequestHandler(FilteredLastPresenzaRequest.class, new FilteredLastPresenzaRequestHandler());
        //endregion

        //region Setters
        NetworkManager.getInstance().addRequestHandler(SetAddettoRequest.class, new SetAddetto());
        NetworkManager.getInstance().addRequestHandler(SetBambinoRequest.class, new SetBambino());
        NetworkManager.getInstance().addRequestHandler(SetContattoRequest.class, new SetContatto());
        NetworkManager.getInstance().addRequestHandler(SetDiagnosiRequest.class, new SetDiagnosi());
        NetworkManager.getInstance().addRequestHandler(SetFornitoreRequest.class, new SetFornitore());
        NetworkManager.getInstance().addRequestHandler(SetGenitoreRequest.class, new SetGenitore());
        NetworkManager.getInstance().addRequestHandler(SetGitaRequest.class, new SetGita());
        NetworkManager.getInstance().addRequestHandler(SetGruppoRequest.class, new SetGruppo());
        NetworkManager.getInstance().addRequestHandler(SetMenuRequest.class, new SetMenu());
        NetworkManager.getInstance().addRequestHandler(SetMezzoDiTrasportoRequest.class, new SetMezzoDiTrasporto());
        NetworkManager.getInstance().addRequestHandler(SetNumeroDiTelefonoRequest.class, new SetNumeroDiTelefono());
        NetworkManager.getInstance().addRequestHandler(SetPastiRequest.class, new SetPasti());
        NetworkManager.getInstance().addRequestHandler(SetPediatraRequest.class, new SetPediatra());
        NetworkManager.getInstance().addRequestHandler(SetPersonaRequest.class, new SetPersona());
        NetworkManager.getInstance().addRequestHandler(SetPianoViaggiRequest.class, new SetPianoViaggi());
        NetworkManager.getInstance().addRequestHandler(SetQuantitaPastoRequest.class, new SetQuantitaPasto());
        NetworkManager.getInstance().addRequestHandler(SetReazioneAvversaRequest.class, new SetReazioneAvversa());
        NetworkManager.getInstance().addRequestHandler(SetRegistroPresenzeRequest.class, new SetRegistroPresenze());
        //endregion

        //region Special
        NetworkManager.getInstance().addRequestHandler(SetPresenzaRequest.class, new SetPresenzaRequestHandler());
        NetworkManager.getInstance().addRequestHandler(FilteredContattoOnlyRequest.class, new FilteredContattoOnlyRequestHandler());
        //endregion

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
