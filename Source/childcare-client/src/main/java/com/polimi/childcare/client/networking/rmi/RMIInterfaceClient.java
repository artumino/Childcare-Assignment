package com.polimi.childcare.client.networking.rmi;

import com.polimi.childcare.client.networking.IClientNetworkInterface;
import com.polimi.childcare.shared.networking.requests.BaseRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.rmi.IRMIServer;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;

public class RMIInterfaceClient implements IClientNetworkInterface
{
    private IRMIServer serverInterface;

    //Non è una vero e proprio indicatore di connessione (dato che RMI è session-less)
    @Override
    public boolean isConnected() {
        return serverInterface != null;
    }

    @Override
    public void connect(String address, int port) throws IOException
    {
        try {
            this.serverInterface = (IRMIServer)LocateRegistry.getRegistry(address, port).lookup(IRMIServer.ENDPOINT);
        } catch (NotBoundException e) {
            System.out.println("Impossibile comunicare con server RMI");
            e.printStackTrace();
            throw new IOException(e);
        }
    }

    @Override
    public void close()
    {
        this.serverInterface = null;
    }

    @Override
    public BaseResponse sendMessage(BaseRequest request)
    {
        if(serverInterface != null)
        {
            try
            {
                BaseResponse response = serverInterface.messageReceived(request);
                return response;
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                this.close();
            }
        }
        return null;
    }

    @Override
    public String toString()
    {
        return "RMI";
    }
}
