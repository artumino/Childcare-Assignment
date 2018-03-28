package com.polimi.childcare.server.networking.rmi;

import com.polimi.childcare.server.networking.BaseServerNetworkInterface;
import com.polimi.childcare.shared.networking.rmi.IRMIServer;
import com.polimi.childcare.shared.networking.requests.BaseRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

import javax.persistence.Transient;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;

public class RMIInterfaceServer extends BaseServerNetworkInterface implements IRMIServer,Serializable
{

    @Transient
    private String address;
    @Transient
    private int port;
    @Transient
    private String boundName;

    @Override
    public void listen(String address, int port) throws IOException
    {
        this.address = address;
        this.port = port;

        //Registra server RMI
        LocateRegistry.createRegistry(port);
        this.boundName = "//" + address + "/" + IRMIServer.ENDPOINT;
        try {
            LocateRegistry.getRegistry(address, port).bind(IRMIServer.ENDPOINT, this);
        } catch (AlreadyBoundException e)
        {
            System.out.println("Failed to bind RMI on " + this.boundName);
            this.boundName = null;
            e.printStackTrace();
        }
    }

    public boolean isRunning() {
        return this.boundName != null;
    }

    @Override
    public void stop()
    {
        if(this.boundName != null)
        {
            try {
                LocateRegistry.getRegistry(address, port).unbind(IRMIServer.ENDPOINT);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
            this.boundName = null;
        }
    }

    @Override
    public BaseResponse messageReceived(BaseRequest request)
    {
        return super.messageReceived(request);
    }
}
