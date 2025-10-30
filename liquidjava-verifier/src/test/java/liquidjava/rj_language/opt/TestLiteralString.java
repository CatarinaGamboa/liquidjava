package liquidjava.rj_language;

import static org.junit.Assert.assertNotEquals;
import org.junit.Test;
import liquidjava.rj_language.ast.LiteralString;

public class TestLiteralString {
    @Test
    public void testLiteralString() {
        LiteralString s1 = new LiteralString("hello");
        LiteralString s2 = new LiteralString("world");
        assertNotEquals(s1.hashCode(), s2.hashCode());
    }
}
