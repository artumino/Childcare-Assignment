package com.polimi.childcare.shared.networking.rmi;

import com.polimi.childcare.shared.networking.requests.BaseRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRMIServer extends Remote
{
    public static final String ENDPOINT = "IRMIServer";
    BaseResponse messageReceived(BaseRequest request) throws RemoteException;
}
