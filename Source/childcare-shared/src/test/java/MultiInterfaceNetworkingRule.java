import com.polimi.childcare.client.shared.networking.IClientNetworkInterface;
import com.polimi.childcare.client.networking.rmi.RMIInterfaceClient;
import com.polimi.childcare.client.shared.networking.sockets.SocketInterfaceClient;
import com.polimi.childcare.server.networking.IServerNetworkInterface;
import com.polimi.childcare.server.networking.NetworkManager;
import com.polimi.childcare.server.networking.rmi.RMIInterfaceServer;
import com.polimi.childcare.server.networking.sockets.SocketInterfaceServer;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import stubs.BambinoListRequestHandler;
import stubs.BambinoListRequestStub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MultiInterfaceNetworkingRule implements MethodRule
{
    public ArrayList<IClientNetworkInterface> clientNetworkInterfaces;
    public NetworkManager manager;

    public MultiInterfaceNetworkingRule()
    {
        this.clientNetworkInterfaces = new ArrayList<>();
    }

    @Override
    public Statement apply(Statement base, FrameworkMethod method, Object target) {
        return new Statement()
        {
            @Override
            public void evaluate() throws Throwable {
                setupServer();


                manager.listen("localhost", new HashMap<IServerNetworkInterface, Integer>(2)
                {{
                    put(new SocketInterfaceServer(), 55503);
                    put(new RMIInterfaceServer(), 55505);
                }});
                try {
                    base.evaluate();
                } finally {
                    //Chiudo le connessioni
                    for(IClientNetworkInterface iClientNetworkInterface : clientNetworkInterfaces)
                        iClientNetworkInterface.close();
                    manager.stop();
                }
            }
        };
    }

    public IClientNetworkInterface createDummyClient(NetworkingRule.TestType testType) throws IOException {
        IClientNetworkInterface clientNetworkInterface;

        switch (testType)
        {
            case RMI:
                clientNetworkInterface = new RMIInterfaceClient();
                break;
            default:
                clientNetworkInterface = new SocketInterfaceClient();
                break;
        }

        clientNetworkInterfaces.add(clientNetworkInterface);
        clientNetworkInterface.connect("localhost", clientNetworkInterface instanceof SocketInterfaceClient ? 55503 : 55505);
        return clientNetworkInterface;
    }

    private void setupServer()
    {
        this.manager = NetworkManager.getInstance();
        manager.addRequestHandler(BambinoListRequestStub.class, new BambinoListRequestHandler());
    }
}
