import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.shared.entities.*;
import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import rules.DatabaseSessionRule;

import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
        Bambino bambino1 = new Bambino("Paolo", "Rossi", "CF", Date.from(Instant.now()), "Nigeria", "Fiorenzuola", "Piacenza", "Nigeriana", "Via Inesistente, 10", (byte)0);
        Genitore genitore1 = new Genitore("Babu", "Bubu", "EHEHEH", Date.from(Instant.now()), "Nigeria", "Casablanca", "Nonloso", "Nigeriana", "Via Inesistente, 10", (byte)0);
        bambino1.setPediatra(pediatra1);   //Senza non passa il test
        Diagnosi diagnosi1 = new Diagnosi(true, bambino1, reazioneavversa1);
        Addetto addetto1 = new Addetto("Lavoratore", "Schiavizzato", "CF", Date.from(Instant.now()), "Italia", "Comune", "Provincia", "Cittadino", "Ressidente: si", (byte)1);
        NumeroTelefono numero = new NumeroTelefono("3333");
        Pasto pasto1 = new Pasto("Minestrina", "Succcosa Minestra in Brodo");

        bambino1.addGenitore(genitore1);

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
            session.insert(pasto1);
            session.insert(fornitore1);
            session.insert(pediatra1);
            session.insert(bambino1);
            session.insert(addetto1);
            session.insert(reazioneavversa1);
            session.insert(diagnosi1);
            session.insert(contatto1);
            session.insert(numero);
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
        bambinoget = DatabaseSession.getInstance().getByID(Bambino.class, idBambino);
        addettoget = DatabaseSession.getInstance().getByID(Addetto.class, idAddetto);
        contattoget = DatabaseSession.getInstance().getByID(Contatto.class, idContatto);
        genitoreget = DatabaseSession.getInstance().getByID(Genitore.class, idGenitore);


        Assert.assertTrue("Controllo che i due oggetti si equivalgano", pastoget.equals(pasto1));   //So che è il messaggio di errore ma lo lascio così per ora
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", fornitoreget.equals(fornitore1));
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", pediatraget.equals(pediatra1));
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", reazioneavversaget.equals(reazioneavversa1));
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", contattoget.equals(contatto1));
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", diagnosiget.equals(diagnosi1));
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", bambinoget.equals(bambino1));
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", addettoget.equals(addetto1));
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
        diagnosiget = DatabaseSession.getInstance().getByID(Diagnosi.class, idDiagnosi);
        bambinoget = DatabaseSession.getInstance().getByID(Bambino.class, idBambino);
        addettoget = DatabaseSession.getInstance().getByID(Addetto.class, idAddetto);
        contattoget = DatabaseSession.getInstance().getByID(Contatto.class, idContatto);

        Assert.assertTrue("Controllo che i due oggetti si equivalgano", pastoget.getNome() == "Eheheheh");
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", fornitoreget.getEmail() == "1233897@live.it");
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", pediatraget.getIndirizzo() == "Cambiato");
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", reazioneavversaget.getNome() == "Grano");
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", contattoget.getIndirizzo() == "Inventato ORA");
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", diagnosiget.getPersona().equals(addetto1));
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", bambinoget.getPediatra().equals(n2));
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", addettoget.getCodiceFiscale() == "CF-------");


        DatabaseSession.getInstance().execute(session ->{
            session.deleteByID(Genitore.class, idGenitore);
            session.deleteByID(Pasto.class, idPasto);
            session.deleteByID(Fornitore.class, idFornitore);
            session.deleteByID(Pediatra.class, idPediatra);
            session.deleteByID(ReazioneAvversa.class, idReazione);
            session.deleteByID(Addetto.class, idAddetto);
            session.deleteByID(Diagnosi.class, idDiagnosi);
            session.deleteByID(Bambino.class, idBambino);
            session.deleteByID(Contatto.class, idContatto);

            return true;
        });

        pastoget = DatabaseSession.getInstance().getByID(Pasto.class, idPasto);
        fornitoreget = DatabaseSession.getInstance().getByID(Fornitore.class, idFornitore);
        pediatraget = DatabaseSession.getInstance().getByID(Pediatra.class, idPediatra);
        reazioneavversaget = DatabaseSession.getInstance().getByID(ReazioneAvversa.class, idReazione);
        diagnosiget = DatabaseSession.getInstance().getByID(Diagnosi.class, idDiagnosi);
        bambinoget = DatabaseSession.getInstance().getByID(Bambino.class, idBambino);
        addettoget = DatabaseSession.getInstance().getByID(Addetto.class, idAddetto);
        contattoget = DatabaseSession.getInstance().getByID(Contatto.class, idContatto);


        Assert.assertTrue("Controllo che i due oggetti si equivalgano", pastoget == null);
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", fornitoreget == null);
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", pediatraget == null);
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", reazioneavversaget == null);
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", contattoget == null);
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", diagnosiget == null);
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", addettoget == null);
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", bambinoget == null);

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
