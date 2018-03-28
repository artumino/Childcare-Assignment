import com.polimi.childcare.shared.networking.requests.NullRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

public class SocketsNetworkingTest
{
    @Rule
    public NetworkingRule netRule = new NetworkingRule(NetworkingRule.TestType.Sockets);

    @Test
    @Ignore
    public void MultipleClientTest() throws IOException
    {
        NetworkTestStub.TestMultipleClients(netRule);
    }

    @Test
    public void TestMultipleClientsPayloadedTransfer() throws IOException
    {
        NetworkTestStub.TestMultipleClientsPayloadedTransfer(netRule);
    }
}
