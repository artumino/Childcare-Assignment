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
    public void insertionTest()
    {
        Fornitore f = new Fornitore("Azienda Testing sui Bambini", "000000","Piacenza","PC1234","non@laha.it","00000", "IT0123456789");
        Pediatra e = new Pediatra("Pediatra Test", "Francesco", "Giovanni", "Via Sesto 2, Cremona");
        Contatto c = new Contatto("Contatto Test", "Ciso", "Giovanni", "Via Sesto 2, Cremona");
        ReazioneAvversa rv = new ReazioneAvversa("Latte", "Se lo bevi muori");
        Bambino b = new Bambino("Paolo", "Rossi", "CF", Date.from(Instant.now()), "Nigeria", "Fiorenzuola", "Piacenza", "Nigeriana", "Via Inesistente, 10", (byte)0);
        b.setPediatra(e);   //Senza non passa il test
        Diagnosi d = new Diagnosi(true, b, rv);
        Addetto a = new Addetto("Lavoratore", "Schiavizzato", "CF", Date.from(Instant.now()), "Italia", "Comune", "Provincia", "Cittadino", "Ressidente: si", (byte)1);
        Pasto p = new Pasto("Minestrina", "Succcosa Minestra in Brodo");

        Pasto r;
        Fornitore i;
        Pediatra o;
        Bambino n;
        Diagnosi k;
        Addetto t;
        ReazioneAvversa m;
        Contatto y;

        DatabaseSession.getInstance().execute(session ->{
            session.insert(p);
            session.insert(f);
            session.insert(e);
            session.insert(b);
            session.insert(a);
            session.insert(rv);
            session.insert(d);
            session.insert(c);

            return true;
        });

        r = DatabaseSession.getInstance().getByID(Pasto.class, 1);
        i = DatabaseSession.getInstance().getByID(Fornitore.class, 1);
        o = DatabaseSession.getInstance().getByID(Pediatra.class, 1);
        m = DatabaseSession.getInstance().getByID(ReazioneAvversa.class, 1);
        k = DatabaseSession.getInstance().getByID(Diagnosi.class, 1);
        n = DatabaseSession.getInstance().getByID(Bambino.class, 1);
        t = DatabaseSession.getInstance().getByID(Addetto.class, 2);
        y = DatabaseSession.getInstance().getByID(Contatto.class, 2);

        Assert.assertTrue("Controllo che i due oggetti si equivalgano", r.equals(p));   //So che è il messaggio di errore ma lo lascio così per ora
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", i.equals(f));
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", o.equals(e));
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", m.equals(rv));
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", y.equals(c));
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", k.equals(d));
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", n.equals(b));
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", t.equals(a));


        Pediatra n2 = new Pediatra("Pediatra Johnny", "Pifferi", "Johnny", "Via Bianchi 2, Piacenza");

        p.setNome("Eheheheh");
        f.setFAX("1233897");
        e.setIndirizzo("Cambiato");
        rv.setNome("Grano");
        d.setPersona(a);
        b.setPediatra(n2);  //Questa non posso provarla perchè è Lazy
        a.setCodiceFiscale("CF-------");
        c.setIndirizzo("Inventato ORA");


        DatabaseSession.getInstance().execute(session ->{
            session.update(p);
            session.update(f);
            session.update(e);
            session.update(b);
            session.update(a);
            session.update(rv);
            session.update(d);
            session.update(c);

            return true;
        });


        r = DatabaseSession.getInstance().getByID(Pasto.class, 1);
        i = DatabaseSession.getInstance().getByID(Fornitore.class, 1);
        o = DatabaseSession.getInstance().getByID(Pediatra.class, 1);
        m = DatabaseSession.getInstance().getByID(ReazioneAvversa.class, 1);
        k = DatabaseSession.getInstance().getByID(Diagnosi.class, 1);
        n = DatabaseSession.getInstance().getByID(Bambino.class, 1);
        t = DatabaseSession.getInstance().getByID(Addetto.class, 2);
        y = DatabaseSession.getInstance().getByID(Contatto.class, 2);

        Assert.assertTrue("Controllo che i due oggetti si equivalgano", r.getNome() == "Eheheheh");
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", i.getFAX() == "1233897");
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", o.getIndirizzo() == "Cambiato");
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", m.getNome() == "Grano");
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", y.getIndirizzo() == "Inventato ORA");
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", k.getPersona().equals(a));
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", t.getCodiceFiscale() == "CF-------");


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

        r = DatabaseSession.getInstance().getByID(Pasto.class, 1);
        i = DatabaseSession.getInstance().getByID(Fornitore.class, 1);
        o = DatabaseSession.getInstance().getByID(Pediatra.class, 1);
        m = DatabaseSession.getInstance().getByID(ReazioneAvversa.class, 1);
        k = DatabaseSession.getInstance().getByID(Diagnosi.class, 1);
        n = DatabaseSession.getInstance().getByID(Bambino.class, 1);
        t = DatabaseSession.getInstance().getByID(Addetto.class, 2);
        y = DatabaseSession.getInstance().getByID(Contatto.class, 2);


        Assert.assertTrue("Controllo che i due oggetti si equivalgano", r == null);
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", i == null);
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", o == null);
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", m == null);
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", y == null);
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", k == null);
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", t == null);

    }

    @Test
    public void deleteTest()    //Ormai superfluo
    {
        Pasto p = new Pasto("Minestrina", "Succcosa Minestra in Brodo");
        DatabaseSession.getInstance().execute(session -> {
            session.insert(p); //Da ora p è PERSISTENT
            Pasto r = session.getByID(Pasto.class, 1);
            System.out.println("ID: " + r.getID() + " - " + "Nome: " + r.getNome() + " - " + "Descrizione: " + r.getDescrizione());
            session.delete(p); //Consentito NO ERRORI
            r = session.getByID(Pasto.class, 1);
            if(r != null)
                System.out.println("ID: " + r.getID() + " - " + "Nome: " + r.getNome() + " - " + "Descrizione: " + r.getDescrizione());

            return true;
        });

        if(DatabaseSession.getInstance().getByID(Pasto.class, 1) == null)
            Assert.assertTrue("Eliminato correttamente elemento creato nel test precendente", true);
        else
            Assert.assertTrue("Non eliminato correttamente elemento creato nel test precendente", false);
    }
}
