package com.polimi.childcare.client.shared.networking.sockets;

import com.polimi.childcare.client.shared.networking.IClientNetworkInterface;
import com.polimi.childcare.client.shared.networking.exceptions.NetworkSerializationException;
import com.polimi.childcare.shared.networking.requests.BaseRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.serialization.SerializationUtils;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Base64;

public class SocketInterfaceClient implements IClientNetworkInterface
{
    private Socket clientSocket;

    //Internal
    private BufferedInputStream inputStream;
    private BufferedOutputStream outputStream;
    private BufferedReader isReader;
    private BufferedWriter osWriter;

    @Override
    public boolean isConnected() {
        return clientSocket != null && clientSocket.isConnected();
    }

    @Override
    public void connect(String address, int port) throws IOException
    {
        this.clientSocket = new Socket();
        this.clientSocket.connect(new InetSocketAddress(address, port), 3000);

        this.inputStream = new BufferedInputStream(this.clientSocket.getInputStream());
        this.outputStream = new BufferedOutputStream(this.clientSocket.getOutputStream());
        this.isReader = new BufferedReader(new InputStreamReader(this.inputStream));
        this.osWriter = new BufferedWriter(new OutputStreamWriter(this.outputStream));
    }

    @Override
    public void close()
    {
        if(this.clientSocket != null)
        {
            //Provo a chiudere gli stream e liberare il socket
            try {
                if(this.inputStream != null)
                    this.inputStream.close();

                if(this.outputStream != null)
                    this.outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    if(!clientSocket.isClosed())
                        this.clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.clientSocket = null;
            }
        }
    }

    @Override
    public BaseResponse sendMessage(BaseRequest request) throws NetworkSerializationException
    {
        try
        {
            if (clientSocket.isConnected()) {

                //Invio la request
                this.osWriter.write(Base64.getEncoder().encodeToString(SerializationUtils.serializeToByteArray(request)));
                this.osWriter.newLine();
                this.osWriter.flush();

                //Attendo una risposta
                String responseStr = this.isReader.readLine();

                //Disconnected!
                if(responseStr == null)
                {
                    this.close();
                    return null;
                }

                byte[] responseBytes = Base64.getDecoder().decode(responseStr);

                //Computo la risposta
                BaseResponse response = SerializationUtils.deserializeByteArray(responseBytes, BaseResponse.class);
                if(response == null)
                    throw new NetworkSerializationException();
                return response;
            }
        } catch (IOException ex)
        {
            ex.printStackTrace();
            this.close();
        }
        return null;
    }

    @Override
    public String toString()
    {
        return "Socket";
    }
}
