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
        Contatto c = new Pediatra("Contatto Test", "Francesco", "Giovanni", "Via Sesto 2, Cremona");
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
        t = DatabaseSession.getInstance().getByID(Addetto.class, 1);
        y = DatabaseSession.getInstance().getByID(Contatto.class, 1);

        Assert.assertTrue("Controllo che i due oggetti si equivalgano", r.equals(p));
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", i.equals(f));
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", o.equals(e));
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", m.equals(rv));
        //Assert.assertTrue("Controllo che i due oggetti si equivalgano", y.equals(c));
        //Assert.assertTrue("Controllo che i due oggetti si equivalgano", k.equals(d));
        //Assert.assertTrue("Controllo che i due oggetti si equivalgano", n.equals(b));
        //Assert.assertTrue("Controllo che i due oggetti si equivalgano", t.equals(a));
    }

    @Test
    public void deleteTest()
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
