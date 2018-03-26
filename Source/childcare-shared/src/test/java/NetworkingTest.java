import com.polimi.childcare.shared.networking.requests.NullRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

public class NetworkingTest
{
    @Rule
    public NetworkingRule netRule = new NetworkingRule(NetworkingRule.TestType.Sockets);

    @Test
    public void MultipleClientsTest() throws IOException
    {
        for(int i = 0; i < 50; i++)
        {
            Assert.assertTrue("Ritornata risposta errata", netRule.createDummyClient().sendMessage(new NullRequest()) != null);
        }
    }
}
