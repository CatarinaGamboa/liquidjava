package liquidjava.rj_language;

import static org.junit.Assert.assertNotEquals;
import org.junit.Test;
import liquidjava.rj_language.ast.LiteralBoolean;

public class TestLiteralBoolean {
    @Test
    public void testHashCode() {
        LiteralBoolean b1 = new LiteralBoolean(true);
        LiteralBoolean b2 = new LiteralBoolean(false);
        assertNotEquals(b1.hashCode(), b2.hashCode());
    }
}