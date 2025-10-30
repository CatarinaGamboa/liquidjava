
package liquidjava.rj_language;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import liquidjava.rj_language.ast.LiteralString;

public class TestLiteralString {
    // Teste fornecido
    @Test
    public void testLiteralString() {
        LiteralString s1 = new LiteralString("hello");
        LiteralString s2 = new LiteralString("world");
        assertNotEquals(s1.hashCode(), s2.hashCode());
    }
}
