import com.polimi.childcare.client.networking.IClientNetworkInterface;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.networking.requests.NullRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import org.junit.Assert;
import stubs.BambinoListRequestStub;
import stubs.BambinoListResponseStub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class NetworkTestStub
{
    public static final String[] names = new String[] { "Pippo", "Mario", "Maria", "Massimo", "Edoardo", "Antonio", "Luisa"};
    public static final String[] surnames = new String[] { "Ermeno", "Di Marzo", "Inpiazza", "Rossi", "Verdi", "Neri", "Blu"};

    public static void TestMultipleClients(NetworkingRule netRule) throws IOException {
        for(int i = 0; i < 50; i++)
        {
            Assert.assertNotNull("Ritornata risposta errata", netRule.createDummyClient().sendMessage(new NullRequest()));
        }
    }

    public static void TestMultipleClientsPayloadedTransfer(NetworkingRule netRule) throws IOException
    {
        Random rnd = new Random();
        for(int i = 0; i < 50; i++)
        {
            ArrayList<Bambino> bambini = new ArrayList<>();
            for(int j = 0; j < rnd.nextInt(5); j++)
            {
                Bambino bambinoTest = new Bambino(names[rnd.nextInt(names.length)],
                        surnames[rnd.nextInt(surnames.length)],
                        surnames[rnd.nextInt(surnames.length)],
                        new Date(), "Italia", "Piacenza", "PC", "Italiana",
                        "via Pisoni Pise 1", (byte)rnd.nextInt(2));
                bambini.add(bambinoTest);
                bambini.add(bambinoTest);
            }
            IClientNetworkInterface client = netRule.createDummyClient();

            BaseResponse baseResponse = client.sendMessage(new BambinoListRequestStub(123, bambini));
            Assert.assertNotNull(baseResponse);
            Assert.assertFalse("Bad Request per una request lecita", baseResponse instanceof BadRequestResponse);
            Assert.assertTrue("Tipo risposta di ritorno errata", baseResponse instanceof BambinoListResponseStub);

            BambinoListResponseStub response = (BambinoListResponseStub)baseResponse;
            Assert.assertEquals("Campo codice alterato", 123, response.getCode());

            List<Bambino> bambiniResponse = response.getPayload();
            Assert.assertNotNull(bambiniResponse);
            Assert.assertEquals("Lughezza payload risposta errato", bambiniResponse.size(), bambini.size());
            for(int k = 0; k < bambini.size(); k++)
                Assert.assertEquals("Valori ritornati alterati o non ordinati", bambiniResponse.get(k), bambini.get(i));
        }
    }
}
