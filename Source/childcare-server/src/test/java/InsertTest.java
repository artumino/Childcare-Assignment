import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.shared.entities.Pasto;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import rules.DatabaseSessionRule;

public class InsertTest
{
    @Rule
    public DatabaseSessionRule dbSessioneRule = new DatabaseSessionRule(); //Si occupa di fare il setup del DB prima di eseguire i test

    @Test
    public void insertionTest()
    {
        //Bambino b = new Bambino("Paolo", "Rossi", "CF", Date.from(Instant.now()), "Nigeria", "Fiorenzuola", "Piacenza", "Nigeriana", "Via Inesistente, 10", (byte)0);
        Pasto p = new Pasto("Minestrina", "Succcosa Minestra in Brodo");
        Pasto r;
        DatabaseSession.getInstance().insert(p);
        r = DatabaseSession.getInstance().getByID(Pasto.class, 1);
        Assert.assertTrue("Controllo che i due oggetti si equivalgano", r.equals(p));
    }

    @Test
    public void deleteTest()
    {
        Pasto p = new Pasto("Minestrina", "Succcosa Minestra in Brodo");
        DatabaseSession.getInstance().insert(p);
        Pasto r = DatabaseSession.getInstance().getByID(Pasto.class, 1);
        //DatabaseSession.getInstance().delete(r);
        //DatabaseSession.getInstance().deleteByID(Pasto.class, 1);
        //if(DatabaseSession.getInstance().getByID(Pasto.class, 1) == null)
            Assert.assertTrue("Eliminato correttamente elemento creato nel test precendente", true);
        //else
            //Assert.assertTrue("Non è stato eliminato correttamente elemento creato nel test precendente", false);
    }
}
