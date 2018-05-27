import com.polimi.childcare.server.Main;
import com.polimi.childcare.server.database.DatabaseDemo;
import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.server.networking.NetworkManager;
import com.polimi.childcare.shared.entities.*;
import com.polimi.childcare.shared.networking.requests.filtered.*;
import com.polimi.childcare.shared.networking.requests.setters.SetBambinoRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListAddettiResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListBambiniResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListContattoResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListFornitoriResponse;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import rules.DatabaseSessionRule;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

public class InsertTest
{
    @Rule
    public DatabaseSessionRule dbSessioneRule = new DatabaseSessionRule(); //Si occupa di fare il setup del DB prima di eseguire i test

    @Test
    public void insertionTest() //Prova del mio branch chiamato Database
    {
        Fornitore fornitore1 = new Fornitore("Azienda Testing sui Bambini", "000000","Piacenza","PC1234","non@laha.it", "IT0123456789");
        Pediatra pediatra1 = new Pediatra("Pediatra Test", "Francesco", "Giovanni", "Via Sesto 2, Cremona");
        Contatto contatto1 = new Contatto("Contatto Test", "Ciso", "Giovanni", "Via Sesto 2, Cremona");
        ReazioneAvversa reazioneavversa1 = new ReazioneAvversa("Latte", "Se lo bevi muori");
        Bambino bambino1 = new Bambino("Paolo", "Rossi", "CF", LocalDateTime.now().toLocalDate(), "Nigeria", "Fiorenzuola", "Piacenza", "Nigeriana", "Via Inesistente, 10", (byte)0);
        Genitore genitore1 = new Genitore("Babu", "Bubu", "EHEHEH", LocalDateTime.now().toLocalDate(), "Nigeria", "Casablanca", "Nonloso", "Nigeriana", "Via Inesistente, 10", (byte)0);
        bambino1.setPediatra(pediatra1);   //Senza non passa il test
        Diagnosi diagnosi1 = new Diagnosi(true, bambino1, reazioneavversa1);
        Addetto addetto1 = new Addetto("Lavoratore", "Schiavizzato", "CF", LocalDateTime.now().toLocalDate(), "Italia", "Comune", "Provincia", "Cittadino", "Ressidente: si", (byte)1);
        String numero = "3333";
        addetto1.addTelefono(numero);
        Pasto pasto1 = new Pasto("Minestrina", "Succcosa Minestra in Brodo");

        bambino1.addGenitore(genitore1);
        pasto1.setFornitore(fornitore1);
        pasto1.addReazione(reazioneavversa1);

        Pasto pastoget;
        Fornitore fornitoreget;
        Pediatra pediatraget;
        Bambino bambinoget;
        Diagnosi diagnosiget;
        Addetto addettoget;
        ReazioneAvversa reazioneavversaget;
        Contatto contattoget;
        Genitore genitoreget;

        DatabaseSession.getInstance().execute(session ->{   //Ordine nel database dipende da insert nella session
            session.insert(fornitore1);
            session.insert(pasto1);
            session.insert(pediatra1);
            session.insert(bambino1);
            session.insert(addetto1);
            session.insert(reazioneavversa1);
            session.insert(diagnosi1);
            session.insert(contatto1);
            session.insert(genitore1);

            return true;
        });

        int idBambino = bambino1.getID();
        int idAddetto = addetto1.getID();
        int idGenitore = genitore1.getID();
        int idPediatra = pediatra1.getID();
        int idContatto = contatto1.getID();
        int idDiagnosi = diagnosi1.getID();
        int idReazione = reazioneavversa1.getID();
        int idPasto = pasto1.getID();
        int idFornitore = fornitore1.getID();

        pastoget = DatabaseSession.getInstance().getByID(Pasto.class, idPasto);
        fornitoreget = DatabaseSession.getInstance().getByID(Fornitore.class, idFornitore);
        pediatraget = DatabaseSession.getInstance().getByID(Pediatra.class, idPediatra);
        reazioneavversaget = DatabaseSession.getInstance().getByID(ReazioneAvversa.class, idReazione);
        diagnosiget = DatabaseSession.getInstance().getByID(Diagnosi.class, idDiagnosi);
        contattoget = DatabaseSession.getInstance().getByID(Contatto.class, idContatto);
        genitoreget = DatabaseSession.getInstance().getByID(Genitore.class, idGenitore, true);
        bambinoget = DatabaseSession.getInstance().getByID(Bambino.class, idBambino, true);
        addettoget = DatabaseSession.getInstance().getByID(Addetto.class, idAddetto, true);

        Assert.assertEquals("Controllo che i due oggetti si equivalgano", pastoget, pasto1);   //So che è il messaggio di errore ma lo lascio così per ora
        Assert.assertEquals("Controllo che i due oggetti si equivalgano", fornitoreget, fornitore1);
        Assert.assertEquals("Controllo che i due oggetti si equivalgano", pediatraget, pediatra1);
        Assert.assertEquals("Controllo che i due oggetti si equivalgano", reazioneavversaget, reazioneavversa1);
        Assert.assertEquals("Controllo che i due oggetti si equivalgano", contattoget, contatto1);
        Assert.assertEquals("Controllo che i due oggetti si equivalgano", diagnosiget, diagnosi1);
        Assert.assertEquals("Controllo che i due oggetti si equivalgano", bambinoget, bambino1);
        Assert.assertEquals("Controllo che i due oggetti si equivalgano", addettoget, addetto1);
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", genitoreget.getBambini().contains(bambinoget));
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", bambinoget.getGenitori().contains(genitoreget));

        Pediatra n2 = new Pediatra("Pediatra Johnny", "Pifferi", "Johnny", "Via Bianchi 2, Piacenza");

        pasto1.setNome("Eheheheh");
        fornitore1.setEmail("1233897@live.it");
        pediatra1.setIndirizzo("Cambiato");
        reazioneavversa1.setNome("Grano");
        diagnosi1.setPersona(addetto1);
        bambino1.setPediatra(n2);
        addetto1.setCodiceFiscale("CF-------");
        contatto1.setIndirizzo("Inventato ORA");


        DatabaseSession.getInstance().execute(session ->{
            session.update(pasto1);
            session.update(fornitore1);
            session.update(pediatra1);
            session.insert(n2);
            session.update(bambino1);
            session.update(addetto1);
            session.update(reazioneavversa1);
            session.update(diagnosi1);
            session.update(contatto1);

            return true;
        });

        pastoget = DatabaseSession.getInstance().getByID(Pasto.class, idPasto);
        fornitoreget = DatabaseSession.getInstance().getByID(Fornitore.class, idFornitore);
        pediatraget = DatabaseSession.getInstance().getByID(Pediatra.class, idPediatra);
        reazioneavversaget = DatabaseSession.getInstance().getByID(ReazioneAvversa.class, idReazione);
        diagnosiget = DatabaseSession.getInstance().getByID(Diagnosi.class, idDiagnosi, true);
        contattoget = DatabaseSession.getInstance().getByID(Contatto.class, idContatto);

        Assert.assertEquals("Controllo che i due oggetti si equivalgano", "Eheheheh", pastoget.getNome());
        Assert.assertEquals("Controllo che i due oggetti si equivalgano", "1233897@live.it", fornitoreget.getEmail());
        Assert.assertEquals("Controllo che i due oggetti si equivalgano", "Cambiato", pediatraget.getIndirizzo());
        Assert.assertEquals("Controllo che i due oggetti si equivalgano", "Grano", reazioneavversaget.getNome());
        Assert.assertEquals("Controllo che i due oggetti si equivalgano", "Inventato ORA", contattoget.getIndirizzo());
        Assert.assertEquals("Controllo che i due oggetti si equivalgano", diagnosiget.getPersona(), addetto1);
        Assert.assertEquals("Controllo che il numero di addetto sia invariato", numero, addettoget.getTelefoni().get(0));

        genitoreget.unsafeRemoveBambino(bambinoget);    //Non va
        bambino1.removeGenitore(genitore1);             //Non va


        DatabaseSession.getInstance().execute(session ->{
            //session.update(genitore1);
            //session.update(bambino1);
            session.deleteByID(Genitore.class, idGenitore);
            session.deleteByID(Fornitore.class, idFornitore);
            session.deleteByID(Pasto.class, idPasto);
            session.deleteByID(Pediatra.class, idPediatra);
            session.deleteByID(ReazioneAvversa.class, idReazione); //FIXME: Controllare Cascade
            session.deleteByID(Diagnosi.class, idDiagnosi);
            session.deleteByID(Addetto.class, idAddetto);
            session.deleteByID(Bambino.class, idBambino);
            session.deleteByID(Contatto.class, idContatto);

            return true;
        });

        pastoget = DatabaseSession.getInstance().getByID(Pasto.class, idPasto);
        fornitoreget = DatabaseSession.getInstance().getByID(Fornitore.class, idFornitore);
        pediatraget = DatabaseSession.getInstance().getByID(Pediatra.class, idPediatra);
        reazioneavversaget = DatabaseSession.getInstance().getByID(ReazioneAvversa.class, idReazione);
        diagnosiget = DatabaseSession.getInstance().getByID(Diagnosi.class, idDiagnosi);
        contattoget = DatabaseSession.getInstance().getByID(Contatto.class, idContatto);
        bambinoget = DatabaseSession.getInstance().getByID(Bambino.class, idBambino);


        Assert.assertNull("Controllo che i due oggetti si equivalgano", pastoget);
        Assert.assertNull("Controllo che i due oggetti si equivalgano", fornitoreget);
        Assert.assertNull("Controllo che i due oggetti si equivalgano", pediatraget);
        Assert.assertNull("Controllo che i due oggetti si equivalgano", reazioneavversaget);
        Assert.assertNull("Controllo che i due oggetti si equivalgano", contattoget);
        Assert.assertNull("Controllo che i due oggetti si equivalgano", diagnosiget);
        Assert.assertNull("Controllo che i due oggetti si equivalgano", bambinoget);

    }

    @Test
    public void deleteTest()    //Ormai superfluo
    {
        Fornitore fornitore1 = new Fornitore("test", "dsadsadas", "test", "prova", "email.email@test.it", "IBAN");
        DatabaseSession.getInstance().execute(session -> {
            session.insert(fornitore1); //Da ora p è PERSISTENT
            Fornitore fornitoreget = session.getByID(Fornitore.class, 1);
            session.delete(fornitore1); //Consentito NO ERRORI

            return true;
        });

        Assert.assertTrue("Eliminato correttamente elemento creato nel test precendente", DatabaseSession.getInstance().getByID(Fornitore.class, 1) == null);
    }

    @Test
    public void demoGeneration()
    {
        DatabaseDemo.runDemoGeneration(100);
    }

    @Test
    public void requestTest()
    {
        Main.initHandlers();
        DatabaseDemo.runDemoGeneration(100);
        BaseResponse bambinoResponse = NetworkManager.getInstance().processRequest(new FilteredBambiniRequest(2,0, true, null, null));
        BaseResponse addettoResponse = NetworkManager.getInstance().processRequest(new FilteredAddettoRequest(1,0, true, null, null));
        BaseResponse contattoResponse = NetworkManager.getInstance().processRequest(new FilteredContattoOnlyRequest(2,0,true, null, null));
        BaseResponse fornitoriResponse = NetworkManager.getInstance().processRequest(new FilteredFornitoriRequest(2,0,true,null,null));

        if(!(bambinoResponse instanceof ListBambiniResponse))
            Assert.fail("Risposta di tipo errato");
        if(!(addettoResponse instanceof ListAddettiResponse))
            Assert.fail("Risposta di tipo errato");
        if(!(contattoResponse instanceof ListContattoResponse))
            Assert.fail("Risposta di tipo errato");
        if(!(fornitoriResponse instanceof ListFornitoriResponse))
            Assert.fail("Risposta di tipo errato");

        Bambino bambinoReq = ((ListBambiniResponse) bambinoResponse).getPayload().get(0);
        Bambino modder = ((ListBambiniResponse) bambinoResponse).getPayload().get(1);
        Addetto dt = ((ListAddettiResponse) addettoResponse).getPayload().get(0);
        List<Contatto> ct = ((ListContattoResponse) contattoResponse).getPayload();
        List<Fornitore> ft = ((ListFornitoriResponse) fornitoriResponse).getPayload();
        Genitore g = new Genitore("Babu", "Bubu", "EHEHEH", LocalDateTime.now().toLocalDate(), "Nigeria", "Casablanca", "Nonloso", "Nigeriana", "Via Inesistente, 10", (byte)0);

        if(!modder.getGenitori().isEmpty())
            modder.removeGenitore((Genitore) modder.getGenitori().toArray()[0]);
        if(!modder.getContatti().isEmpty()) {
            Contatto temp = DatabaseSession.getInstance().getByID(Contatto.class, ((Contatto) modder.getContatti().toArray()[0]).getID(), true);
            temp.removeBambino(modder);
            DatabaseSession.getInstance().update(temp);
            modder.unsafeRemoveContatto(temp);
        }

        Contatto contatto1 = new Contatto("Contatto Test", "Ciso", "Giovanni", "Via Sesto 2, Cremona");
        contatto1.addBambino(modder);
        DatabaseSession.getInstance().insertOrUpdate(contatto1);
        modder.unsafeAddContatto(contatto1);

        modder.setPediatra(null);

        DatabaseSession.getInstance().insert(g);
        modder.addGenitore(g);
        DatabaseSession.getInstance().delete(dt);

        for(Contatto contatto : ct)
            DatabaseSession.getInstance().delete(contatto);


        for(Fornitore fornitore : ft)
            DatabaseSession.getInstance().delete(fornitore);

        DatabaseSession.getInstance().insertOrUpdate(modder);
        BaseResponse response = NetworkManager.getInstance().processRequest(new SetBambinoRequest(bambinoReq, true, bambinoReq.consistecyHashCode()));
        Bambino b = DatabaseSession.getInstance().getByID(Bambino.class, ((ListBambiniResponse) bambinoResponse).getPayload().get(0).getID(), true);
        Assert.assertNull(b);
        Assert.assertEquals(200, response.getCode());
    }
}
