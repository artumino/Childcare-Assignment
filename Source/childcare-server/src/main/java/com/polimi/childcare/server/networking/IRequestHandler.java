package com.polimi.childcare.server.networking;

import com.polimi.childcare.shared.networking.requests.BaseRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

import java.io.Serializable;

public interface IRequestHandler<T extends BaseRequest> extends Serializable
{
    BaseResponse processRequest(T request);
}
