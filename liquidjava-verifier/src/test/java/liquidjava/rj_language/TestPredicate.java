package liquidjava.rj_language;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TestPredicate {

    @Test
    public void testCreateInvocation() {
        Predicate p = Predicate.createInvocation("test", Predicate.createVar("x"));
        assertEquals("test(x)", p.getExpression().toString());
    }
}
