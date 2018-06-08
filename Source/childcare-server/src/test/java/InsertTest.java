import com.polimi.childcare.server.Main;
import com.polimi.childcare.server.database.DatabaseDemo;
import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.networking.NetworkManager;
import com.polimi.childcare.shared.entities.*;
import com.polimi.childcare.shared.networking.requests.filtered.*;
import com.polimi.childcare.shared.networking.requests.setters.*;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.*;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import rules.DatabaseSessionRule;

import java.time.LocalDateTime;

public class InsertTest
{
    @Rule
    public DatabaseSessionRule dbSessioneRule = new DatabaseSessionRule(); //Si occupa di fare il setup del DB prima di eseguire i test

    @Test
    public void insertionTest()
    {
        Main.initHandlers();

        Fornitore fornitore1 = new Fornitore("Azienda Testing sui Bambini", "000000","Piacenza","PC1234","non@laha.it", "IT0123456789");
        Pediatra pediatra1 = new Pediatra("Pediatra Test", "Francesco", "Giovanni", "Via Sesto 2, Cremona");
        Contatto contatto1 = new Contatto("Contatto Test", "Ciso", "Giovanni", "Via Sesto 2, Cremona");
        ReazioneAvversa reazioneavversa1 = new ReazioneAvversa("Latte", "Se lo bevi muori");
        Bambino bambino1 = new Bambino("Paolo", "Rossi", "CF", LocalDateTime.now().toLocalDate(), "Nigeria", "Fiorenzuola", "Piacenza", "Nigeriana", "Via Inesistente, 10", Persona.ESesso.Maschio);
        Genitore genitore1 = new Genitore("Babu", "Bubu", "EHEHEH", LocalDateTime.now().toLocalDate(), "Nigeria", "Casablanca", "Nonloso", "Nigeriana", "Via Inesistente, 10", Persona.ESesso.Maschio);
        Addetto addetto1 = new Addetto("Lavoratore", "Schiavizzato", "CF", LocalDateTime.now().toLocalDate(), "Italia", "Comune", "Provincia", "Cittadino", "Ressidente: si", Persona.ESesso.Femmina);
        String numero = "3333";
        addetto1.addTelefono(numero);
        Pasto pasto1 = new Pasto("Minestrina", "Succcosa Minestra in Brodo");

        Assert.assertEquals("Errore nell'inserimento del Fornitore!",NetworkManager.getInstance().processRequest(new SetFornitoreRequest(fornitore1)).getCode(), 200);
        Assert.assertEquals("Errore nell'inserimento della Reazione!",NetworkManager.getInstance().processRequest(new SetReazioneAvversaRequest(reazioneavversa1)).getCode(), 200);
        Assert.assertEquals("Errore nell'inserimento del Pediatra!",NetworkManager.getInstance().processRequest(new SetPediatraRequest(pediatra1)).getCode(), 200);
        Assert.assertEquals("Errore nell'inserimento del Genitore!",NetworkManager.getInstance().processRequest(new SetGenitoreRequest(genitore1)).getCode(), 200);

        ListPediatraResponse pediatraget = (ListPediatraResponse) NetworkManager.getInstance().processRequest(new FilteredPediatraRequest(1,0, true));
        ListFornitoriResponse fornitoreget = (ListFornitoriResponse) NetworkManager.getInstance().processRequest(new FilteredFornitoriRequest(1,0, true));
        ListReazioneAvversaResponse reazioneavversaget = (ListReazioneAvversaResponse) NetworkManager.getInstance().processRequest(new FilteredReazioneAvversaRequest(1,0, true));
        ListGenitoriResponse genitoreget = (ListGenitoriResponse) NetworkManager.getInstance().processRequest(new FilteredGenitoriRequest(1,0, true));

        bambino1.addGenitore(genitoreget.getPayload().get(0));
        bambino1.setPediatra(pediatraget.getPayload().get(0));
        pasto1.setFornitore(fornitoreget.getPayload().get(0));
        pasto1.addReazione(reazioneavversaget.getPayload().get(0));

        Assert.assertEquals("Errore nell'inserimento del Bambino!",NetworkManager.getInstance().processRequest(new SetBambinoRequest(bambino1)).getCode(), 200);
        ListBambiniResponse bambinoget = (ListBambiniResponse) NetworkManager.getInstance().processRequest(new FilteredBambiniRequest(1,0, true));
        genitoreget = (ListGenitoriResponse) NetworkManager.getInstance().processRequest(new FilteredGenitoriRequest(1,0, true));

        Diagnosi diagnosi1 = new Diagnosi(true, bambinoget.getPayload().get(0), reazioneavversaget.getPayload().get(0));

        Assert.assertEquals("Errore nell'inserimento del Pasto!",NetworkManager.getInstance().processRequest(new SetPastiRequest(pasto1)).getCode(), 200);
        Assert.assertEquals("Errore nell'inserimento della Diagnosi!",NetworkManager.getInstance().processRequest(new SetDiagnosiRequest(diagnosi1)).getCode(), 200);
        Assert.assertEquals("Errore nell'inserimento del Contatto!",NetworkManager.getInstance().processRequest(new SetContattoRequest(contatto1)).getCode(), 200);
        Assert.assertEquals("Errore nell'inserimento dell' Addetto!",NetworkManager.getInstance().processRequest(new SetAddettoRequest(addetto1)).getCode(), 200);

        ListPastiResponse pastoget = (ListPastiResponse) NetworkManager.getInstance().processRequest(new FilteredPastoRequest(1,0, true));
        ListDiagnosiResponse diagnosiget = (ListDiagnosiResponse) NetworkManager.getInstance().processRequest(new FilteredDiagnosiRequest(1,0, true));
        ListAddettiResponse addettoget = (ListAddettiResponse) NetworkManager.getInstance().processRequest(new FilteredAddettoRequest(1,0, true));
        ListContattoResponse contattoget = (ListContattoResponse) NetworkManager.getInstance().processRequest(new FilteredContattoOnlyRequest(0,0, true));

        Assert.assertEquals("Controllo che i due oggetti si equivalgano", pastoget.getPayload().get(0).getNome(), pasto1.getNome());
        Assert.assertEquals("Controllo che i due oggetti si equivalgano", fornitoreget.getPayload().get(0).getNumeroRegistroImprese(), fornitore1.getNumeroRegistroImprese());
        Assert.assertEquals("Controllo che i due oggetti si equivalgano", pediatraget.getPayload().get(0).getNome(), pediatra1.getNome());
        Assert.assertEquals("Controllo che i due oggetti si equivalgano", reazioneavversaget.getPayload().get(0).getDescrizione(), reazioneavversa1.getDescrizione());
        Assert.assertEquals("Controllo che i due oggetti si equivalgano", contattoget.getPayload().get(0).getCognome(), contatto1.getCognome());
        Assert.assertEquals("Controllo che i due oggetti si equivalgano", diagnosiget.getPayload().get(0).getPersona().getCodiceFiscale(), diagnosi1.getPersona().getCodiceFiscale());
        Assert.assertEquals("Controllo che i due oggetti si equivalgano", bambinoget.getPayload().get(0).getDataNascita(), bambino1.getDataNascita());
        Assert.assertEquals("Controllo che i due oggetti si equivalgano", addettoget.getPayload().get(0).getTelefoni(), addetto1.getTelefoni());
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", genitoreget.getPayload().get(0).getBambini().contains(bambinoget.getPayload().get(0)));
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", bambinoget.getPayload().get(0).getGenitori().contains(genitoreget.getPayload().get(0)));

        Pediatra n2 = new Pediatra("Pediatra Johnny", "Pifferi", "Johnny", "Via Bianchi 2, Piacenza");

        pasto1 = pastoget.getPayload().get(0);
        fornitore1 = fornitoreget.getPayload().get(0);
        pediatra1 = pediatraget.getPayload().get(0);
        reazioneavversa1 = reazioneavversaget.getPayload().get(0);
        diagnosi1 = diagnosiget.getPayload().get(0);
        addetto1= addettoget.getPayload().get(0);
        contatto1 = contattoget.getPayload().get(0);

        pasto1.setNome("Eheheheh");
        fornitore1.setEmail("1233897@live.it");
        pediatra1.setIndirizzo("Cambiato");
        reazioneavversa1.setNome("Grano");
        diagnosi1.setPersona(addetto1);
        addetto1.setCodiceFiscale("CF-------");
        contatto1.setIndirizzo("Inventato ORA");
        bambino1 = bambinoget.getPayload().get(0);


        Assert.assertEquals("Errore nell'inserimento del Pediatra!",NetworkManager.getInstance().processRequest(new SetPediatraRequest(n2)).getCode(), 200);
        pediatraget = (ListPediatraResponse) NetworkManager.getInstance().processRequest(new FilteredPediatraRequest(2,0, true));
        bambino1.setPediatra(pediatraget.getPayload().get(1));

        Assert.assertEquals("Errore nell'inserimento del Fornitore!",NetworkManager.getInstance().processRequest(new SetFornitoreRequest(fornitore1)).getCode(), 200);
        Assert.assertEquals("Errore nell'inserimento della Reazione!",NetworkManager.getInstance().processRequest(new SetReazioneAvversaRequest(reazioneavversa1)).getCode(), 200);
        Assert.assertEquals("Errore nell'inserimento del Pasto!",NetworkManager.getInstance().processRequest(new SetPastiRequest(pasto1)).getCode(), 200);
        Assert.assertEquals("Errore nell'inserimento del Pediatra!",NetworkManager.getInstance().processRequest(new SetPediatraRequest(pediatra1)).getCode(), 200);
        Assert.assertEquals("Errore nell'inserimento del Genitore!",NetworkManager.getInstance().processRequest(new SetGenitoreRequest(genitore1)).getCode(), 200);
        Assert.assertEquals("Errore nell'inserimento del Bambino!",NetworkManager.getInstance().processRequest(new SetBambinoRequest(bambino1)).getCode(), 200);
        Assert.assertEquals("Errore nell'inserimento della Diagnosi!",NetworkManager.getInstance().processRequest(new SetDiagnosiRequest(diagnosi1)).getCode(), 200);
        Assert.assertEquals("Errore nell'inserimento del Contatto!",NetworkManager.getInstance().processRequest(new SetContattoRequest(contatto1)).getCode(), 200);
        Assert.assertEquals("Errore nell'inserimento dell' Addetto!",NetworkManager.getInstance().processRequest(new SetAddettoRequest(addetto1)).getCode(), 200);

        pastoget = (ListPastiResponse) NetworkManager.getInstance().processRequest(new FilteredPastoRequest(1,0, true));
        fornitoreget = (ListFornitoriResponse) NetworkManager.getInstance().processRequest(new FilteredFornitoriRequest(1,0, true));
        diagnosiget = (ListDiagnosiResponse) NetworkManager.getInstance().processRequest(new FilteredDiagnosiRequest(1,0, true));
        addettoget = (ListAddettiResponse) NetworkManager.getInstance().processRequest(new FilteredAddettoRequest(1,0, true));
        reazioneavversaget = (ListReazioneAvversaResponse) NetworkManager.getInstance().processRequest(new FilteredReazioneAvversaRequest(1,0, true));
        contattoget = (ListContattoResponse) NetworkManager.getInstance().processRequest(new FilteredContattoOnlyRequest(0,0, true));
        pediatraget = (ListPediatraResponse) NetworkManager.getInstance().processRequest(new FilteredPediatraRequest(2,0, true));

        Assert.assertEquals("Controllo che i due oggetti si equivalgano", "Eheheheh", pastoget.getPayload().get(0).getNome());
        Assert.assertEquals("Controllo che i due oggetti si equivalgano", "1233897@live.it", fornitoreget.getPayload().get(0).getEmail());
        Assert.assertEquals("Controllo che i due oggetti si equivalgano", "Cambiato", pediatraget.getPayload().get(0).getIndirizzo());
        Assert.assertEquals("Controllo che i due oggetti si equivalgano", "Grano", reazioneavversaget.getPayload().get(0).getNome());
        Assert.assertEquals("Controllo che i due oggetti si equivalgano", "Inventato ORA", contattoget.getPayload().get(0).getIndirizzo());
        Assert.assertEquals("Controllo che i due oggetti si equivalgano", diagnosiget.getPayload().get(0).getPersona().getCognome(), addettoget.getPayload().get(0).getCognome());
        Assert.assertEquals("Controllo che il numero di addetto sia invariato", numero, addettoget.getPayload().get(0).getTelefoni().get(0));
        Assert.assertEquals("Controllo che i due oggetti si equivalgano", "Inventato ORA", contattoget.getPayload().get(0).getIndirizzo());
    }

    @Test
    public void deleteTest()
    {
        Main.initHandlers();
        DatabaseDemo.runDemoGeneration(100);

        BaseResponse bambinoResponse = NetworkManager.getInstance().processRequest(new FilteredBambiniRequest(10,0, true));
        BaseResponse addettoResponse = NetworkManager.getInstance().processRequest(new FilteredAddettoRequest(5,0, true));
        BaseResponse fornitoriResponse = NetworkManager.getInstance().processRequest(new FilteredFornitoriRequest(20,0,true));

        if(!(bambinoResponse instanceof ListBambiniResponse))
            Assert.fail("Risposta di tipo errato");
        if(!(addettoResponse instanceof ListAddettiResponse))
            Assert.fail("Risposta di tipo errato");
        if(!(fornitoriResponse instanceof ListFornitoriResponse))
            Assert.fail("Risposta di tipo errato");

        SetBambinoRequest bambinoreq;
        SetAddettoRequest addettoreq;
        SetContattoRequest contattoreq;
        SetFornitoreRequest fornitoreq;

        BaseResponse risposta;

        for(Bambino b : ((ListBambiniResponse) bambinoResponse).getPayload())
        {
            bambinoreq = new SetBambinoRequest(b, true, 0);
            risposta = NetworkManager.getInstance().processRequest(bambinoreq);
            Assert.assertEquals("Errore nell'eliminazione di un Bambino!" ,risposta.getCode(), 200);
        }

        for(Addetto ad : ((ListAddettiResponse) addettoResponse).getPayload())
        {
            addettoreq = new SetAddettoRequest(ad, true, 0);
            risposta = NetworkManager.getInstance().processRequest(addettoreq);
            Assert.assertEquals("Errore nell'eliminazione di un Addetto!" ,risposta.getCode(), 200);
        }


        for(Fornitore f : ((ListFornitoriResponse) fornitoriResponse).getPayload())
        {
            fornitoreq = new SetFornitoreRequest(f, true, 0);
            risposta = NetworkManager.getInstance().processRequest(fornitoreq);
            Assert.assertEquals("Errore nell'eliminazione di un Fornitore!" ,risposta.getCode(), 200);
        }


        BaseResponse contattoResponse = NetworkManager.getInstance().processRequest(new FilteredContattoOnlyRequest(20,0,true));

        if(!(contattoResponse instanceof ListContattoResponse))
            Assert.fail("Risposta di tipo errato");

        for(Contatto c : ((ListContattoResponse) contattoResponse).getPayload())
        {
            contattoreq = new SetContattoRequest(c, true, 0);
            risposta = NetworkManager.getInstance().processRequest(contattoreq);
            Assert.assertEquals("Errore nell'eliminazione di un Contatto!" ,risposta.getCode(), 200);
        }
    }

    @Test
    public void demoGeneration()
    {
        DatabaseDemo.runDemoGeneration(100);
    }

    @Test
    public void updateTest()
    {
        Main.initHandlers();
        DatabaseDemo.runDemoGeneration(100);
        BaseResponse bambinoResponse = NetworkManager.getInstance().processRequest(new FilteredBambiniRequest(2,0, true));

        if(!(bambinoResponse instanceof ListBambiniResponse))
            Assert.fail("Risposta di tipo errato");

        Bambino bambinoReq = ((ListBambiniResponse) bambinoResponse).getPayload().get(0);
        String stringa = "OGGI";
        bambinoReq.setNome(stringa);

        SetBambinoRequest s = new SetBambinoRequest(bambinoReq);
        BaseResponse response = NetworkManager.getInstance().processRequest(s);
        Assert.assertEquals(response.getCode(), 200);
        Bambino db = DatabaseSession.getInstance().getByID(Bambino.class, bambinoReq.getID());
        Assert.assertEquals("Non ha updato!", db.getNome(), stringa);

    }
}
