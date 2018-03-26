package com.polimi.childcare.shared.networking;

import com.polimi.childcare.shared.networking.requests.BaseRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

//Interfaccia per l'astrazione dei componenti di rete
public interface IClientNetworkInterface
{
    void connect(String address, int port);
    void close();
    BaseResponse sendMessage(BaseRequest request);
}
