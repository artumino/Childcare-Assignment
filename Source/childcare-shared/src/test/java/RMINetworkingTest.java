import com.polimi.childcare.shared.networking.requests.NullRequest;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

public class RMINetworkingTest
{
    @Rule
    public NetworkingRule netRule = new NetworkingRule(NetworkingRule.TestType.RMI);

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
