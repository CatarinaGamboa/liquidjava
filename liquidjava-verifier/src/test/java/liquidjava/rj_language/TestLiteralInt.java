package liquidjava.rj_language;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import liquidjava.rj_language.ast.LiteralInt;

public class TestLiteralInt {
    @Test
    public void testGetValue() {
        LiteralInt n = new LiteralInt(5);
        assertEquals(5, n.getValue());
    }
}