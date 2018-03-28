package stubs.networking;

import com.polimi.childcare.server.networking.BaseServerNetworkInterface;
import com.polimi.childcare.server.networking.rmi.RMIInterfaceServer;
import com.polimi.childcare.shared.networking.requests.BaseRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.rmi.IRMIServer;

import javax.persistence.Transient;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class RMIInterfaceServerStub extends RMIInterfaceServer
{
    @Override
    public BaseResponse messageReceived(BaseRequest request) {
        //Aggiungo tipi risposta per messaggi di test
        BaseResponse testResponse = ServerStub.messageReceived(request);
        return testResponse != null ? testResponse : super.messageReceived(request);
    }
}
