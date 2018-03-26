package com.polimi.childcare.shared.networking;

import com.polimi.childcare.shared.networking.requests.BaseRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

import java.io.IOException;

//Interfaccia per l'astrazione dei componenti di rete
public interface IClientNetworkInterface
{
    void connect(String address, int port) throws IOException;
    void close();

    //Metodo bloccante per l'invio di un messaggio e l'attesa della risposta
    BaseResponse sendMessage(BaseRequest request);
}
