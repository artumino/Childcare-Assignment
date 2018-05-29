import com.polimi.childcare.client.shared.networking.IClientNetworkInterface;
import com.polimi.childcare.client.shared.networking.exceptions.NetworkSerializationException;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.entities.Persona;
import com.polimi.childcare.shared.networking.requests.NullRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import org.junit.Assert;
import stubs.BambinoListRequestStub;
import stubs.BambinoListResponseStub;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class NetworkTestStub
{
    public static final String[] names = new String[] { "Pippo", "Mario", "Maria", "Massimo", "Edoardo", "Antonio", "Luisa"};
    public static final String[] surnames = new String[] { "Ermeno", "Di Marzo", "Inpiazza", "Rossi", "Verdi", "Neri", "Blu"};

    public static ArrayList<Bambino> createBambiniList()
    {
        Random rnd = new Random();
        ArrayList<Bambino> bambini = new ArrayList<>();
        for(int j = 0; j < rnd.nextInt(5); j++)
        {
            Bambino bambinoTest = new Bambino(NetworkTestStub.names[rnd.nextInt(NetworkTestStub.names.length)],
                    NetworkTestStub.surnames[rnd.nextInt(NetworkTestStub.surnames.length)],
                    NetworkTestStub.surnames[rnd.nextInt(NetworkTestStub.surnames.length)],
                    LocalDateTime.now().toLocalDate(), "Italia", "Piacenza", "PC", "Italiana",
                    "via Pisoni Pise 1", Persona.ESesso.Maschio);
            bambini.add(bambinoTest);
            bambini.add(bambinoTest);
        }
        return bambini;
    }

    public static void TestMultipleClients(NetworkingRule netRule) throws IOException {
        for(int i = 0; i < 50; i++)
        {
            try {
                Assert.assertNotNull("Ritornata risposta errata", netRule.createDummyClient().sendMessage(new NullRequest()));
            } catch (NetworkSerializationException e) {
                e.printStackTrace();
                Assert.fail("Errore di serializzazione durante sendMessage");
            }
        }
    }

    public static void TestMultipleClientsPayloadedTransfer(NetworkingRule netRule) throws IOException
    {
        for(int i = 0; i < 50; i++)
        {
            ArrayList<Bambino> bambini = createBambiniList();
            IClientNetworkInterface client = netRule.createDummyClient();

            BaseResponse baseResponse = null;
            try {
                baseResponse = client.sendMessage(new BambinoListRequestStub(123, bambini));
            } catch (NetworkSerializationException e) {
                e.printStackTrace();
                Assert.fail("Errore di serializzazione durante sendMessage");
            }
            TestMultipleClientsPayloadedAsserts(bambini, baseResponse);
        }
    }

    public static void TestMultipleClientsPayloadedAsserts(ArrayList<Bambino> bambini, BaseResponse baseResponse)
    {
        Assert.assertNotNull(baseResponse);
        Assert.assertFalse("Bad Request per una request lecita", baseResponse instanceof BadRequestResponse);
        Assert.assertTrue("Tipo risposta di ritorno errata", baseResponse instanceof BambinoListResponseStub);

        BambinoListResponseStub response = (BambinoListResponseStub)baseResponse;
        Assert.assertEquals("Campo codice alterato", 123, response.getCode());

        List<Bambino> bambiniResponse = response.getPayload();
        Assert.assertNotNull(bambiniResponse);
        Assert.assertEquals("Lughezza payload risposta errato", bambiniResponse.size(), bambini.size());
        for(int k = 0; k < bambini.size(); k++)
            Assert.assertEquals("Valori ritornati alterati o non ordinati", bambiniResponse.get(k), bambini.get(k));
    }
}
