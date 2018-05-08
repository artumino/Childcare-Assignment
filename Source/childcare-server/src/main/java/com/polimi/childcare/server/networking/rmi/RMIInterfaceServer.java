package com.polimi.childcare.server.networking.rmi;

import com.polimi.childcare.server.networking.BaseServerNetworkInterface;
import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.networking.rmi.IRMIServer;
import com.polimi.childcare.shared.networking.requests.BaseRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;

public class RMIInterfaceServer extends BaseServerNetworkInterface implements IRMIServer,Serializable
{
    private transient String address;
    private transient int port;
    private transient Registry boundRegistry;

    @Override
    public void listen(String address, int port, IRequestHandler defaultHandler) throws IOException
    {
        System.out.println("Avvio interfaccia di rete RMI...");

        super.listen(address, port, defaultHandler);
        this.address = address;
        this.port = port;

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }

        try {
            //Registra server RMI
            this.boundRegistry = LocateRegistry.createRegistry(port);
        }
        catch (ExportException ex)
        {
            this.boundRegistry = LocateRegistry.getRegistry(port);
        }
        finally
        {
            try
            {
                this.boundRegistry.bind(IRMIServer.ENDPOINT, this);
            }
            catch (AlreadyBoundException e1)
            {
                e1.printStackTrace();
                try {
                    IRMIServer irmiServer = this;
                    IRMIServer stub = (IRMIServer)UnicastRemoteObject.exportObject(irmiServer, 0);
                    this.boundRegistry.rebind(IRMIServer.ENDPOINT, stub);
                }
                catch (Exception ex)
                {
                    System.out.println("Failed to bind RMI on " + this.boundRegistry);
                    this.boundRegistry = null;
                    ex.printStackTrace();
                }
            }
        }
    }

    public boolean isRunning() {
        return this.boundRegistry != null;
    }

    @Override
    public void stop()
    {
        if(this.boundRegistry != null)
        {
            try {
                LocateRegistry.getRegistry(address, port).unbind(IRMIServer.ENDPOINT);

                //Chiudo il registro
                UnicastRemoteObject.unexportObject(this.boundRegistry, true);

            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
            this.boundRegistry = null;
        }
    }

    @Override
    public BaseResponse messageReceived(BaseRequest request)
    {
        return super.messageReceived(request);
    }
}
