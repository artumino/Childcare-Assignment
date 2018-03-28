package stubs.networking;

import com.polimi.childcare.server.networking.BaseServerNetworkInterface;
import com.polimi.childcare.server.networking.sockets.SocketClientHandler;
import com.polimi.childcare.server.networking.sockets.SocketInterfaceServer;
import com.polimi.childcare.server.networking.sockets.dummyrequests.DummyConnectionClosedRequest;
import com.polimi.childcare.shared.networking.requests.BaseRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class SocketInterfaceServerStub extends SocketInterfaceServer
{
    @Override
    public BaseResponse messageReceived(BaseRequest request) {
        //Aggiungo tipi risposta per messaggi di test
        BaseResponse testResponse = ServerStub.messageReceived(request);
        return testResponse != null ? testResponse : super.messageReceived(request);
    }
}
