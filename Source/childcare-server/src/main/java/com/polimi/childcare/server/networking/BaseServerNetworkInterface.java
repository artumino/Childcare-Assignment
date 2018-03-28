package com.polimi.childcare.server.networking;

import com.polimi.childcare.shared.networking.requests.BaseRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

public abstract class BaseServerNetworkInterface implements IServerNetworkInterface
{
    @Override
    public BaseResponse messageReceived(BaseRequest request)
    {
        // Codice comune per la gestione contemporanea di più interfacce
        return new BadRequestResponse(); //Default 403 BAD REQUEST
    }
}
