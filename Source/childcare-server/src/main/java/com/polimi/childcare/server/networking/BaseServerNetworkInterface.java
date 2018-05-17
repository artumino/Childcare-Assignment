package com.polimi.childcare.server.networking;

import com.polimi.childcare.shared.networking.requests.BaseRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

import java.io.IOException;
import java.io.Serializable;

public abstract class BaseServerNetworkInterface implements IServerNetworkInterface
{
    private transient IRequestHandler<BaseRequest>  defaultHandler;

    @Override
    public void listen(String address, int port, IRequestHandler<BaseRequest> defaultHandler) throws IOException
    {
        if(defaultHandler == null)
            throw new DefaultRequestHandlerNotBoundException();
        this.defaultHandler = defaultHandler;
    }

    //Accessore per le classi derivate che controlla che il processo sia assegnato
    public IRequestHandler<BaseRequest>  getDefaultRequestHandler()
    {
        if(defaultHandler == null)
            throw new DefaultRequestHandlerNotBoundException();

        return defaultHandler;
    }


    @Override
    public BaseResponse messageReceived(BaseRequest request)
    {
        try
        {
            return getDefaultRequestHandler().processRequest(request);
        }
        catch (DefaultRequestHandlerNotBoundException ex)
        {
            ex.printStackTrace();
        }

        return new BadRequestResponse(); //Default 403 BAD REQUEST
    }
}
