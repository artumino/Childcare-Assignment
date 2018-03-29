import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.shared.entities.*;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import rules.DatabaseSessionRule;

import java.time.Instant;
import java.util.Date;

public class InsertTest
{
    @Rule
    public DatabaseSessionRule dbSessioneRule = new DatabaseSessionRule(); //Si occupa di fare il setup del DB prima di eseguire i test

    @Test
    public void insertionTest() //Prova del mio branch chiamato Database
    {
        Fornitore fornitore1 = new Fornitore("Azienda Testing sui Bambini", "000000","Piacenza","PC1234","non@laha.it","00000", "IT0123456789");
        Pediatra pediatra1 = new Pediatra("Pediatra Test", "Francesco", "Giovanni", "Via Sesto 2, Cremona");
        Contatto contatto1 = new Contatto("Contatto Test", "Ciso", "Giovanni", "Via Sesto 2, Cremona");
        ReazioneAvversa reazioneavversa1 = new ReazioneAvversa("Latte", "Se lo bevi muori");
        Bambino bambino1 = new Bambino("Paolo", "Rossi", "CF", Date.from(Instant.now()), "Nigeria", "Fiorenzuola", "Piacenza", "Nigeriana", "Via Inesistente, 10", (byte)0);
        bambino1.setPediatra(pediatra1);   //Senza non passa il test
        Diagnosi diagnosi1 = new Diagnosi(true, bambino1, reazioneavversa1);
        Addetto addetto1 = new Addetto("Lavoratore", "Schiavizzato", "CF", Date.from(Instant.now()), "Italia", "Comune", "Provincia", "Cittadino", "Ressidente: si", (byte)1);
        Pasto pasto1 = new Pasto("Minestrina", "Succcosa Minestra in Brodo");

        Pasto pastoget;
        Fornitore fornitoreget;
        Pediatra pediatraget;
        Bambino bambinoget;
        Diagnosi diagnosiget;
        Addetto addettoget;
        ReazioneAvversa reazioneavversaget;
        Contatto contattoget;

        DatabaseSession.getInstance().execute(session ->{
            session.insert(pasto1);
            session.insert(fornitore1);
            session.insert(pediatra1);
            session.insert(bambino1);
            session.insert(addetto1);
            session.insert(reazioneavversa1);
            session.insert(diagnosi1);
            session.insert(contatto1);

            return true;
        });

        pastoget = DatabaseSession.getInstance().getByID(Pasto.class, 1);
        fornitoreget = DatabaseSession.getInstance().getByID(Fornitore.class, 1);
        pediatraget = DatabaseSession.getInstance().getByID(Pediatra.class, 1);
        reazioneavversaget = DatabaseSession.getInstance().getByID(ReazioneAvversa.class, 1);
        diagnosiget = DatabaseSession.getInstance().getByID(Diagnosi.class, 1);
        bambinoget = DatabaseSession.getInstance().getByID(Bambino.class, 1);
        addettoget = DatabaseSession.getInstance().getByID(Addetto.class, 2);
        contattoget = DatabaseSession.getInstance().getByID(Contatto.class, 2);

        Assert.assertTrue("Controllo che i due oggetti si equivalgano", pastoget.equals(pasto1));   //So che è il messaggio di errore ma lo lascio così per ora
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", fornitoreget.equals(fornitore1));
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", pediatraget.equals(pediatra1));
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", reazioneavversaget.equals(reazioneavversa1));
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", contattoget.equals(contatto1));
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", diagnosiget.equals(diagnosi1));
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", bambinoget.equals(bambino1));
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", addettoget.equals(addetto1));


        Pediatra n2 = new Pediatra("Pediatra Johnny", "Pifferi", "Johnny", "Via Bianchi 2, Piacenza");

        pasto1.setNome("Eheheheh");
        fornitore1.setFAX("1233897");
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
            session.update(bambino1);
            session.update(addetto1);
            session.update(reazioneavversa1);
            session.update(diagnosi1);
            session.update(contatto1);

            return true;
        });


        pastoget = DatabaseSession.getInstance().getByID(Pasto.class, 1);
        fornitoreget = DatabaseSession.getInstance().getByID(Fornitore.class, 1);
        pediatraget = DatabaseSession.getInstance().getByID(Pediatra.class, 1);
        reazioneavversaget = DatabaseSession.getInstance().getByID(ReazioneAvversa.class, 1);
        diagnosiget = DatabaseSession.getInstance().getByID(Diagnosi.class, 1);
        bambinoget = DatabaseSession.getInstance().getByID(Bambino.class, 1);
        addettoget = DatabaseSession.getInstance().getByID(Addetto.class, 2);
        contattoget = DatabaseSession.getInstance().getByID(Contatto.class, 2);

        Assert.assertTrue("Controllo che i due oggetti si equivalgano", pastoget.getNome() == "Eheheheh");
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", fornitoreget.getFAX() == "1233897");
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", pediatraget.getIndirizzo() == "Cambiato");
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", reazioneavversaget.getNome() == "Grano");
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", contattoget.getIndirizzo() == "Inventato ORA");
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", diagnosiget.getPersona().equals(addetto1));
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", bambinoget.getPediatra().equals(n2));
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", addettoget.getCodiceFiscale() == "CF-------");


        DatabaseSession.getInstance().execute(session ->{
            session.deleteByID(Pasto.class, 1);
            session.deleteByID(Fornitore.class, 1);
            session.deleteByID(Pediatra.class, 1);
            session.deleteByID(ReazioneAvversa.class, 1);
            session.deleteByID(Diagnosi.class, 1);
            session.deleteByID(Bambino.class, 1);
            session.deleteByID(Addetto.class, 2);
            session.deleteByID(Contatto.class, 1);
            session.deleteByID(Contatto.class, 2);

            return true;
        });

        pastoget = DatabaseSession.getInstance().getByID(Pasto.class, 1);
        fornitoreget = DatabaseSession.getInstance().getByID(Fornitore.class, 1);
        pediatraget = DatabaseSession.getInstance().getByID(Pediatra.class, 1);
        reazioneavversaget = DatabaseSession.getInstance().getByID(ReazioneAvversa.class, 1);
        diagnosiget = DatabaseSession.getInstance().getByID(Diagnosi.class, 1);
        bambinoget = DatabaseSession.getInstance().getByID(Bambino.class, 1);
        addettoget = DatabaseSession.getInstance().getByID(Addetto.class, 2);
        contattoget = DatabaseSession.getInstance().getByID(Contatto.class, 2);


        Assert.assertTrue("Controllo che i due oggetti si equivalgano", pastoget == null);
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", fornitoreget == null);
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", pediatraget == null);
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", reazioneavversaget == null);
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", contattoget == null);
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", diagnosiget == null);
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", addettoget == null);

    }

    @Test
    public void deleteTest()    //Ormai superfluo
    {
        Pasto pasto1 = new Pasto("Minestrina", "Succcosa Minestra in Brodo");
        DatabaseSession.getInstance().execute(session -> {
            session.insert(pasto1); //Da ora p è PERSISTENT
            Pasto pastoget = session.getByID(Pasto.class, 1);
            System.out.println("ID: " + pastoget.getID() + " - " + "Nome: " + pastoget.getNome() + " - " + "Descrizione: " + pastoget.getDescrizione());
            session.delete(pasto1); //Consentito NO ERRORI
            pastoget = session.getByID(Pasto.class, 1);
            if(pastoget != null)
                System.out.println("ID: " + pastoget.getID() + " - " + "Nome: " + pastoget.getNome() + " - " + "Descrizione: " + pastoget.getDescrizione());

            return true;
        });

        Assert.assertTrue("Eliminato correttamente elemento creato nel test precendente", DatabaseSession.getInstance().getByID(Pasto.class, 1) == null);
    }
}
