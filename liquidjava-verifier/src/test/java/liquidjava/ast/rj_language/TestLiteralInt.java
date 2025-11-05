package liquidjava.rj_language;

import static org.junit.Assert.assertNotEquals;
import org.junit.Test;
import liquidjava.rj_language.ast.LiteralInt;

public class TestLiteralInt {
    @Test
    public void testLiteralInt() {

        LiteralInt a = new LiteralInt(10);
        LiteralInt b = new LiteralInt(20);

        assertNotEquals(a.hashCode(), b.hashCode());
    }
}