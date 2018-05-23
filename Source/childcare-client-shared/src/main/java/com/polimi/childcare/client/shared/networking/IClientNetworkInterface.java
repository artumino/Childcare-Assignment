package com.polimi.childcare.client.shared.networking;

import com.polimi.childcare.client.shared.networking.exceptions.NetworkSerializationException;
import com.polimi.childcare.shared.networking.requests.BaseRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

import java.io.IOException;

//Interfaccia per l'astrazione dei componenti di rete
public interface IClientNetworkInterface
{
    boolean isConnected();
    void connect(String address, int port) throws IOException;
    void close();

    //Metodo bloccante per l'invio di un messaggio e l'attesa della risposta
    BaseResponse sendMessage(BaseRequest request) throws NetworkSerializationException;
}
