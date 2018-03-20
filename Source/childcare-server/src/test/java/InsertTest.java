import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.entities.Pasto;
import org.junit.Test;

import java.time.Instant;
import java.util.Date;

public class InsertTest
{
    @Test
    public void insertionTest()
    {
        //Bambino b = new Bambino("Paolo", "Rossi", "CF", Date.from(Instant.now()), "Nigeria", "Fiorenzuola", "Piacenza", "Nigeriana", "Via Inesistente, 10", (byte)0);
        DatabaseSession.getInstance().setUp();
        Pasto p = new Pasto("Minestrina", "Succcosa Minestra in Brodo");
        DatabaseSession.getInstance().insert(p);
    }
}
