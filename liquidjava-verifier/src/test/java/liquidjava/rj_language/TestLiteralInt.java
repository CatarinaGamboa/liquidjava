package liquidjava.rj_language;

import static org.junit.Assert.assertNotEquals;
import org.junit.Test;
import liquidjava.rj_language.ast.LiteralInt;

public class TestLiteralInt {
    @Test
    public void testLiteralString() {
        LiteralInt n1 = new LiteralInt(8);
        LiteralInt n2 = new LiteralInt(2);
        assertNotEquals(n1.hashCode(), n2.hashCode());
    }
}
