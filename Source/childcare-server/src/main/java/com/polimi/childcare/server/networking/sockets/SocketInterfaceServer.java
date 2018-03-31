package com.polimi.childcare.server.networking.sockets;

import com.polimi.childcare.server.networking.BaseServerNetworkInterface;
import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.server.networking.sockets.dummyrequests.DummyConnectionClosedRequest;
import com.polimi.childcare.shared.networking.requests.BaseRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class SocketInterfaceServer extends BaseServerNetworkInterface implements Runnable
{
    private ServerSocket serverSocket;
    private Thread listenerThread;
    private boolean isRunning;

    private ArrayList<SocketClientHandler> handlers;

    @Override
    public void listen(String address, int port, IRequestHandler defaultHandler) throws IOException
    {
        System.out.println("Avvio interfaccia di rete socket...");

        super.listen(address, port, defaultHandler);
        handlers = new ArrayList<>(3);
        serverSocket = new ServerSocket(port);
        isRunning = true;
        listenerThread = new Thread(this);
        listenerThread.start();
    }

    @Override
    public void stop()
    {
        isRunning = false;

        //Prova a fermare thread
        if(listenerThread != null)
            listenerThread.interrupt();

        //Provo a chiudere le connessioni con tutti gli handler attivi
        //Devo duplicare la lista perchè ogni handler.close() rimuove l'handler da handlers
        ArrayList<SocketClientHandler> currentHandlers = new ArrayList<>(handlers.size());
        currentHandlers.addAll(handlers);
        for(SocketClientHandler handler : currentHandlers)
        {
            if(handler.isRunning())
                handler.close();
        }

        //Provo a liberare il socket su cui sono in ascolto
        if(serverSocket != null && !serverSocket.isClosed())
        {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public BaseResponse messageReceived(BaseRequest request)
    {
        //Un client si è disconnesso
        if(request instanceof DummyConnectionClosedRequest)
        {
            //Rimuovo il client dalla lista di handler
            SocketClientHandler clientHandler = ((DummyConnectionClosedRequest) request).getHandler();
            if(clientHandler != null && this.handlers.contains(clientHandler))
                this.handlers.remove(clientHandler);
        }

        return super.messageReceived(request);
    }

    @Override
    public void run()
    {
        System.out.println("Inizio l'ascolto per dei client su " + serverSocket.getLocalSocketAddress().toString());
        try
        {
            while (isRunning) {
                //Ascolto per un client
                Socket socket = serverSocket.accept();

                //Creo un handler per il client e lo avvio
                SocketClientHandler handler = new SocketClientHandler(socket, this);
                this.handlers.add(handler);
                Thread thread = new Thread(handler);
                thread.start();
            }
        }
        catch (SocketException ignored) { }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        finally {
            //Interrompe il server
            this.stop();
        }
    }
}
