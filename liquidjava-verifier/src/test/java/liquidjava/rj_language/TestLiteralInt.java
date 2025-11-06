package liquidjava.rj_language;

import static org.junit.Assert.assertNotEquals;
import org.junit.Test;
import liquidjava.rj_language.ast.LiteralInt;

ublic class TestLiteralInt {
    @Test
    public void testToString() {
        LiteralInt n = new LiteralInt(61801);
        assertEquals("61801", n.toString());
    }
}