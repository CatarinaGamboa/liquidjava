package liquidjava.rj_language;

import static org.junit.Assert.assertNotEquals;
import org.junit.Test;

import liquidjava.rj_language.ast.LiteralBoolean;

public class testLiteralBoolean {

    @Test
    public void testLiteralBoolean() {
        LiteralBoolean s1 = new LiteralBoolean(true);
        LiteralBoolean s2 = new LiteralBoolean(false);
        assertNotEquals(s1.hashCode(), s2.hashCode());
    }
}
