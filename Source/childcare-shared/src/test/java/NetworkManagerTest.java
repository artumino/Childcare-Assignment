import com.polimi.childcare.client.shared.networking.IClientNetworkInterface;
import com.polimi.childcare.client.shared.networking.exceptions.NetworkSerializationException;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.networking.requests.NullRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import stubs.BambinoListRequestStub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class NetworkManagerTest
{
    @Rule
    public MultiInterfaceNetworkingRule netRule = new MultiInterfaceNetworkingRule();

    @Test
    public void MultipleClientTest() throws IOException
    {
        for(int i = 0; i < 50; i++)
        {
            try {
                Assert.assertNotNull("Ritornata risposta errata", netRule.createDummyClient(i % 2 == 0 ? NetworkingRule.TestType.Sockets : NetworkingRule.TestType.RMI).sendMessage(new NullRequest()));
            } catch (NetworkSerializationException e) {
                e.printStackTrace();
                Assert.fail("Send message errore di serializzazione!");
            }
        }
    }

    @Test
    public void TestMultipleClientsPayloadedTransfer() throws IOException
    {
        Random rnd = new Random();
        for(int i = 0; i < 50; i++)
        {
            ArrayList<Bambino> bambini = NetworkTestStub.createBambiniList();
            IClientNetworkInterface client = netRule.createDummyClient(i % 2 == 0 ? NetworkingRule.TestType.Sockets : NetworkingRule.TestType.RMI);

            BaseResponse baseResponse = null;
            try {
                baseResponse = client.sendMessage(new BambinoListRequestStub(123, bambini));
            } catch (NetworkSerializationException e) {
                e.printStackTrace();
            }
            NetworkTestStub.TestMultipleClientsPayloadedAsserts(bambini, baseResponse);
        }
    }
}
