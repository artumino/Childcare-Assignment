package com.polimi.childcare.server.networking.sockets;

import com.polimi.childcare.server.networking.sockets.dummyrequests.DummyConnectionClosedRequest;
import com.polimi.childcare.server.networking.IServerNetworkInterface;
import com.polimi.childcare.shared.networking.requests.BaseRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.serialization.SerializationUtils;

import java.io.*;
import java.net.Socket;
import java.util.Base64;

public class SocketClientHandler implements Runnable
{
    private IServerNetworkInterface networkInterface;
    private Socket socket;

    //Internal
    private BufferedInputStream inputStream;
    private BufferedOutputStream outputStream;
    private BufferedReader isReader;
    private BufferedWriter osWriter;
    private boolean running;

    SocketClientHandler(Socket socket, IServerNetworkInterface networkInterface) throws IOException
    {
        this.networkInterface = networkInterface;
        this.socket = socket;
        this.inputStream = new BufferedInputStream(socket.getInputStream());
        this.outputStream = new BufferedOutputStream(socket.getOutputStream());
        this.isReader = new BufferedReader(new InputStreamReader(this.inputStream));
        this.osWriter = new BufferedWriter(new OutputStreamWriter(this.outputStream));
    }

    public boolean isRunning() { return this.running; }

    @Override
    public void run()
    {
        System.out.println("Accettata connessione da " + socket.getRemoteSocketAddress().toString() + "!");
        running = true;
        try
        {
            while(running)
            {
                //Ascolto per una request codificata in Base64
                String request = isReader.readLine();

                //Se la connessione termina mentre sono in ascolto per una richiesta, request ritorna null
                if(request != null)
                {
                    byte[] requestBytes = Base64.getDecoder().decode(request);
                    BaseRequest requestInstance = SerializationUtils.deserializeByteArray(requestBytes, BaseRequest.class);

                    //Computo una risposta opportuna
                    BaseResponse response = null;
                    if (requestInstance != null)
                        response = networkInterface.messageReceived(requestInstance); //Process Request

                    if (response == null)
                        response = new BaseResponse(400); //Bad Request

                    //Rispondo con una Response codificata in Base64
                    osWriter.write(Base64.getEncoder().encodeToString(SerializationUtils.serializeToByteArray(response)));
                    osWriter.newLine();
                    osWriter.flush();
                }
            }
        } catch (IOException ex)
        {

            if(!ex.getMessage().contains("Socket closed") && !ex.getMessage().contains("Stream closed") ) {
                if (socket != null)
                    System.err.println("Errore nella comunicazione con il client " + socket.getRemoteSocketAddress().toString());
                ex.printStackTrace();
            }
        }
    }

    public void close()
    {
        if(socket != null)
            System.out.println("Chiusa connessione con client" + socket.getRemoteSocketAddress());

        //Fermo ciclo di listen
        running = false;

        //Notifico l'interfaccia di rete che mi sono disconnesso
        networkInterface.messageReceived(new DummyConnectionClosedRequest(this));

        //Libero le risorse del socket
        if(socket != null && socket.isConnected())
        {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                //Prova almeno a chiudere il socket
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
