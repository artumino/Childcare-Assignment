import com.polimi.childcare.server.Main;
import com.polimi.childcare.server.database.DatabaseDemo;
import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.server.networking.NetworkManager;
import com.polimi.childcare.shared.entities.*;
import com.polimi.childcare.shared.networking.requests.filtered.*;
import com.polimi.childcare.shared.networking.requests.setters.SetGitaRequest;
import com.polimi.childcare.shared.networking.requests.setters.SetGruppoRequest;
import com.polimi.childcare.shared.networking.requests.setters.SetMezzoDiTrasportoRequest;
import com.polimi.childcare.shared.networking.requests.special.GeneratePianiViaggioRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.*;
import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import rules.DatabaseSessionRule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class GitaTests
{
    @Rule
    public DatabaseSessionRule dbSessioneRule = new DatabaseSessionRule(); //Si occupa di fare il setup del DB prima di eseguire i test

    @Test
    public void creationTest()
    {
        Main.initHandlers();

        int groupNumber = 5;
        DatabaseDemo.runDemoGeneration(20*groupNumber);

        //Prende bambini
        BaseResponse response = NetworkManager.getInstance().processRequest(new FilteredBambiniRequest(0,0, false));
        ListBambiniResponse bambiniResponse = (ListBambiniResponse)response;
        List<Bambino> allBambini = bambiniResponse.getPayload();

        //Prende addetti
        response = NetworkManager.getInstance().processRequest(new FilteredAddettoRequest(0,0, false));
        ListAddettiResponse addettoResponse = (ListAddettiResponse)response;
        List<Addetto> allAddetti = addettoResponse.getPayload();


        //Prende addetti
        response = NetworkManager.getInstance().processRequest(new FilteredFornitoriRequest(0,0, false));
        ListFornitoriResponse fornitoriResponse = (ListFornitoriResponse)response;
        List<Fornitore> allFornitori = fornitoriResponse.getPayload();

        Assert.assertTrue(allFornitori.size() > 0);
        Fornitore fornitore = allFornitori.get(0);

        Gita gita = new Gita(LocalDate.now(), LocalDate.now().plusDays(1), "Parma", 15);
        response = NetworkManager.getInstance().processRequest(new SetGitaRequest(gita, false, gita.hashCode()));
        Assert.assertEquals(200, response.getCode());

        Session session = DatabaseSession.getInstance().openSession();
        Optional<Gita> getGita = DatabaseSession.getInstance().stream(Gita.class, session).findFirst();
        Assert.assertTrue(getGita.isPresent());
        gita = getGita.get();
        DBHelper.recursiveObjectInitialize(gita);
        session.close();

        Random rnd = new Random();

        //Genero i mezzi di trasporto
        List<MezzoDiTrasporto> mezziDiTrasporto = new ArrayList<>();
        for(int i = 0; i < groupNumber; i++)
            mezziDiTrasporto.add(new MezzoDiTrasporto("TARGA" + i, 25, 1000, 10, fornitore));

        for(MezzoDiTrasporto dummyMezzoDiTrasporto : mezziDiTrasporto)
        {
            response = NetworkManager.getInstance().processRequest(new SetMezzoDiTrasportoRequest(dummyMezzoDiTrasporto, false, dummyMezzoDiTrasporto.consistecyHashCode()));
            Assert.assertEquals(200, response.getCode());
        }

        response = NetworkManager.getInstance().processRequest(new FilteredMezzoDiTrasportoRequest(0, 0, false));
        ListMezzoDiTrasportoResponse mezzoResponse = (ListMezzoDiTrasportoResponse)response;
        List<MezzoDiTrasporto> allMezzi = mezzoResponse.getPayload();

        Assert.assertTrue(allMezzi.size() > groupNumber);

        session = DatabaseSession.getInstance().openSession();
        allMezzi = DatabaseSession.getInstance().stream(MezzoDiTrasporto.class, session).sorted((o1, o2) -> -Integer.compare(o1.getID(), o2.getID())).limit(groupNumber).collect(Collectors.toList());
        Assert.assertEquals(groupNumber, allMezzi.size());
        session.close();

        //Genero i gruppi
        List<Gruppo> dummyGruppoList = new ArrayList<>();
        for(int i = 0; i < groupNumber; i++)
        {
            Gruppo gruppo = new Gruppo(allAddetti.get(rnd.nextInt(allAddetti.size())));
            for(int j = i * (allBambini.size() / groupNumber); j < (i+1) * (allBambini.size() / groupNumber); j++)
                gruppo.unsafeAddBambino(allBambini.get(j));
            dummyGruppoList.add(gruppo);
        }

        for(Gruppo dummyGruppo : dummyGruppoList)
        {
            response = NetworkManager.getInstance().processRequest(new SetGruppoRequest(dummyGruppo, false, dummyGruppo.consistecyHashCode()));
            Assert.assertEquals(200, response.getCode());
        }

        response = NetworkManager.getInstance().processRequest(new FilteredGruppoRequest(0, 0, true));
        ListGruppoResponse gruppiResponse = (ListGruppoResponse)response;
        List<Gruppo> allGruppi = gruppiResponse.getPayload();

        Assert.assertEquals(5, allGruppi.size());

        for(Gruppo gruppo : allGruppi)
            Assert.assertEquals(100/groupNumber, gruppo.getBambini().size());

        //Genero 3 scenari di gite comuni

        //Normale in autobus
        HashMap<Gruppo, MezzoDiTrasporto> gitaPerfettaAutobus = new HashMap<>();
        for(int i = 0; i < groupNumber; i++)
            gitaPerfettaAutobus.put(allGruppi.get(i), allMezzi.get(i));

        clearPianoViaggiGita(gita);
        response = NetworkManager.getInstance().processRequest(new GeneratePianiViaggioRequest(gita, gitaPerfettaAutobus));
        Assert.assertEquals(200, response.getCode());

        //Sovrappopolata
        gita = DatabaseSession.getInstance().getByID(Gita.class, gita.getID(), true);
        clearPianoViaggiGita(gita);
        HashMap<Gruppo, MezzoDiTrasporto> gitaErrataAutobus = new HashMap<>();
        for(int i = 0; i < groupNumber; i++)
            gitaErrataAutobus.put(allGruppi.get(i), allMezzi.get(i/2));
        response = NetworkManager.getInstance().processRequest(new GeneratePianiViaggioRequest(gita, gitaErrataAutobus));
        Assert.assertEquals(403, response.getCode());

        //A piedi
        gita = DatabaseSession.getInstance().getByID(Gita.class, gita.getID(), true);
        clearPianoViaggiGita(gita);
        HashMap<Gruppo, MezzoDiTrasporto> gitaAPiedi = new HashMap<>();
        for(int i = 0; i < groupNumber; i++)
            gitaAPiedi.put(allGruppi.get(i), null);
        response = NetworkManager.getInstance().processRequest(new GeneratePianiViaggioRequest(gita, gitaAPiedi));
        Assert.assertEquals(200, response.getCode());
    }

    private static void clearPianoViaggiGita(Gita gita)
    {
        for(PianoViaggi viaggi : gita.getPianiViaggi())
            gita.unsafeRemovePianoViaggi(viaggi);
        BaseResponse response = NetworkManager.getInstance().processRequest(new SetGitaRequest(gita, false, gita.consistecyHashCode()));
        Assert.assertEquals(response.getCode(), 200);
    }
}
