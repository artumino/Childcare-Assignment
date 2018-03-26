import com.polimi.childcare.client.networking.sockets.SocketInterfaceClient;
import com.polimi.childcare.server.networking.sockets.SocketInterfaceServer;
import com.polimi.childcare.shared.networking.IClientNetworkInterface;
import com.polimi.childcare.shared.networking.IServerNetworkInterface;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

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
        setupServer();
    }

    @Override
    public Statement apply(Statement base, FrameworkMethod method, Object target) {
        return new Statement()
        {
            @Override
            public void evaluate() throws Throwable {
                serverNetworkInterface.listen("localhost", 55403);
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
                //break; //Non implementato
            default:
                clientNetworkInterface = new SocketInterfaceClient();
                break;
        }

        clientNetworkInterfaces.add(clientNetworkInterface);
        clientNetworkInterface.connect("localhost", 55403);
        return clientNetworkInterface;
    }

    private void setupServer()
    {
        switch (testType)
        {
            case RMI:
                //break; //Non implementato
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
