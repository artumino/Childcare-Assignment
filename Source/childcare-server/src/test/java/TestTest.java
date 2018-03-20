import com.polimi.childcare.server.database.DatabaseSession;
import org.hibernate.Session;
import org.junit.Test;

public class TestTest
{
    @Test
    public void testTheTest()
    {
        DatabaseSession.getInstance().setUp();

        Session session = DatabaseSession.getInstance().getSession();
        org.junit.Assert.assertNotNull(session);
        org.junit.Assert.assertEquals(DatabaseSession.getInstance().getCurrentConnectionURL(), "jdbc:mariadb://192.168.0.108:3306/childcare_test_db");
        session.close();
    }
}
