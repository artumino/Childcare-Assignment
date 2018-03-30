package com.polimi.childcare.server.networking;

import com.polimi.childcare.shared.networking.requests.BaseRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

import java.io.Serializable;

/**
 * Interfaccia per un gestore di una richiesta di tipo generico T che estende {@link BaseRequest} proveniente da un client
*/
public interface IRequestHandler<T extends BaseRequest> extends Serializable
{
    /**
     * Il metodo deve processare la richiesta e ritornare una risposta opportuna.
     * Il ritorno di null implica l'inoltro di una {@link com.polimi.childcare.shared.networking.responses.BadRequestResponse} al client.
     * @param request Richiesta di tipo T che estende {@link BaseRequest}
     * @return Risposta da inoltrare al client
     */
    BaseResponse processRequest(T request);
}
