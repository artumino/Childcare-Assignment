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
            return serverInterface.messageReceived(request);
        return null;
    }
}
