package com.polimi.childcare.server.networking;

import com.polimi.childcare.shared.networking.requests.BaseRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

import java.io.IOException;
import java.io.Serializable;

//Interfaccia per l'astrazione dei componenti di rete
public interface IServerNetworkInterface
{
    //Potremmo implementare un meccanismo per le push notification ma sarebbe vincolato ai Socket (a meno di aprire un server RMI su ogni client)
    void listen(String address, int port, IRequestHandler<BaseRequest> defaultHandler) throws IOException;
    void stop();
    boolean isRunning();
    BaseResponse messageReceived(BaseRequest request);
}
