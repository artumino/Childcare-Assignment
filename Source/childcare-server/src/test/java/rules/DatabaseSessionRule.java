package rules;

import com.polimi.childcare.server.database.DatabaseSession;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

public class DatabaseSessionRule implements MethodRule
{
    @Override
    public Statement apply(Statement base, FrameworkMethod method, Object target) {
        return new Statement()
        {

            @Override
            public void evaluate() throws Throwable {
                DatabaseSession.getInstance().setUp();
                try {
                    base.evaluate();
                } finally {
                    DatabaseSession.getInstance().close();
                }
            }
        };
    }
}
