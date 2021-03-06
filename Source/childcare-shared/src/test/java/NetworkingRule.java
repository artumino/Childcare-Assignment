import com.polimi.childcare.client.networking.rmi.RMIInterfaceClient;
import com.polimi.childcare.client.shared.networking.sockets.SocketInterfaceClient;
import com.polimi.childcare.server.networking.NetworkManager;
import com.polimi.childcare.server.networking.rmi.RMIInterfaceServer;
import com.polimi.childcare.server.networking.sockets.SocketInterfaceServer;
import com.polimi.childcare.client.shared.networking.IClientNetworkInterface;
import com.polimi.childcare.server.networking.IServerNetworkInterface;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import stubs.BambinoListRequestHandler;
import stubs.BambinoListRequestStub;

import java.io.IOException;
import java.util.ArrayList;

public class NetworkingRule implements MethodRule
{
    private TestType testType;
    public IServerNetworkInterface serverNetworkInterface;
    public ArrayList<IClientNetworkInterface> clientNetworkInterfaces;

    public NetworkingRule(TestType testType)
    {
        this.clientNetworkInterfaces = new ArrayList<>();
        this.testType = testType;
    }

    @Override
    public Statement apply(Statement base, FrameworkMethod method, Object target) {
        return new Statement()
        {
            @Override
            public void evaluate() throws Throwable {
                setupServer();
                serverNetworkInterface.listen("localhost", testType == TestType.Sockets ? 55403 : 55405, NetworkManager.getInstance());
                try {
                    base.evaluate();
                } finally {
                    //Chiudo le connessioni
                    for(IClientNetworkInterface iClientNetworkInterface : clientNetworkInterfaces)
                        iClientNetworkInterface.close();
                    serverNetworkInterface.stop();
                }
            }
        };
    }

    public IClientNetworkInterface createDummyClient() throws IOException {
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
        clientNetworkInterface.connect("localhost", testType == TestType.Sockets ? 55403 : 55405);
        return clientNetworkInterface;
    }

    private void setupServer()
    {
        NetworkManager.getInstance().addRequestHandler(BambinoListRequestStub.class, new BambinoListRequestHandler());

        switch (testType)
        {
            case RMI:
                serverNetworkInterface = new RMIInterfaceServer();
                break;
            case Sockets:
                serverNetworkInterface = new SocketInterfaceServer();
                break;
        }
    }

    public enum TestType
    {
        Sockets, RMI
    }
}
