package com.polimi.childcare.server.networking;

import com.polimi.childcare.shared.networking.requests.BaseRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

import java.io.IOException;
import java.io.Serializable;

//Interfaccia per l'astrazione dei componenti di rete
public interface IServerNetworkInterface
{
    //Potremmo implementare un meccanismo per le push notification ma sarebbe vincolato ai Socket (a meno di aprire un server RMI su ogni client)

    /**
     * Metodo per iniziare l'ascolto per nuove connessioni in avvio, ogni driver di rete effettuerà il suo setup e arriverà
     * all'ascolto per nuovi client.
     *
     * @param address Indirizzo su cui l'interfaccia deve ascoltare per connessioni in arrivo (per le interfacce che lo permettono)
     * @param port Porta su cui ascoltare per connessioni in arrivo
     * @param defaultHandler Interfaccia di tipo {@link IRequestHandler<BaseRequest>} che fornisca un modo per gestire le richieste dai client
     * @throws IOException In caso di errori nel binding dell'interfaccia
     * @throws DefaultRequestHandlerNotBoundException Quando non è stato impostato il parametro {@param defaultHandler}
     */
    void listen(String address, int port, IRequestHandler<BaseRequest> defaultHandler) throws IOException;

    /**
     * Metodo per fermare l'interfaccia e liberare ogni risorsa allocata dai sottostanti driver di rete
     */
    void stop();

    /**
     * @return Indica se l'interfaccia è attualmente in ascolto o sta gestendo connessioni con client
     */
    boolean isRunning();

    /**
     * Metodo invocato dai client per l'inoltro delle richieste all'handler delle richieste opportuno
     * @param request Richiesta inviata dal client (Deserializzata)
     * @return Risposta da inoltrare al client
     */
    BaseResponse messageReceived(BaseRequest request);
}
