import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.shared.entities.*;
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
        NumeroTelefono numero = new NumeroTelefono("3333");
        addetto1.addTelefono(numero);
        Pasto pasto1 = new Pasto("Minestrina", "Succcosa Minestra in Brodo");

        bambino1.addGenitore(genitore1);
        pasto1.addFornitore(fornitore1);
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
        NumeroTelefono telefonoget;

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
        int idTelefono = numero.getID();

        pastoget = DatabaseSession.getInstance().getByID(Pasto.class, idPasto);
        fornitoreget = DatabaseSession.getInstance().getByID(Fornitore.class, idFornitore);
        pediatraget = DatabaseSession.getInstance().getByID(Pediatra.class, idPediatra);
        reazioneavversaget = DatabaseSession.getInstance().getByID(ReazioneAvversa.class, idReazione);
        diagnosiget = DatabaseSession.getInstance().getByID(Diagnosi.class, idDiagnosi);
        contattoget = DatabaseSession.getInstance().getByID(Contatto.class, idContatto);
        genitoreget = DatabaseSession.getInstance().getByID(Genitore.class, idGenitore, true);
        bambinoget = DatabaseSession.getInstance().getByID(Bambino.class, idBambino, true);
        addettoget = DatabaseSession.getInstance().getByID(Addetto.class, idAddetto, true);
        telefonoget = DatabaseSession.getInstance().getByID(NumeroTelefono.class, idTelefono);

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


        DatabaseSession.getInstance().execute(session ->{
            session.deleteByID(NumeroTelefono.class, idTelefono);
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


        Assert.assertNull("Controllo che i due oggetti si equivalgano", pastoget);
        Assert.assertNull("Controllo che i due oggetti si equivalgano", fornitoreget);
        Assert.assertNull("Controllo che i due oggetti si equivalgano", pediatraget);
        Assert.assertNull("Controllo che i due oggetti si equivalgano", reazioneavversaget);
        Assert.assertNull("Controllo che i due oggetti si equivalgano", contattoget);
        Assert.assertNull("Controllo che i due oggetti si equivalgano", diagnosiget);

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
