package com.polimi.childcare.server.networking.sockets.dummyrequests;

import com.polimi.childcare.server.networking.sockets.SocketClientHandler;
import com.polimi.childcare.shared.networking.requests.BaseRequest;

import java.net.Socket;

//Request usata solo a livello di driver rete per gestire la chiusura delle connessioni (rimane quindi nascosta dal layer di astrazione)
public class DummyConnectionClosedRequest extends BaseRequest
{
    private SocketClientHandler handler;

    public DummyConnectionClosedRequest(SocketClientHandler handler)
    {
        super();
        this.handler = handler;
    }

    public SocketClientHandler getHandler() {
        return handler;
    }
}
