package liquidjava.rj_language;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import liquidjava.rj_language.ast.LiteralString;

public class TestLiteralString {
    @Test
    public void testLiteralString() {
        LiteralString s1 = new LiteralString("hello");
        LiteralString s2 = new LiteralString("world");
        assertNotEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    public void testLiteralStringEquals() {
        LiteralString s1 = new LiteralString("hello");
        LiteralString s2 = new LiteralString("world");
        LiteralString s3 = new LiteralString("hello");
        assertTrue(s1.equals(s3));
        assertFalse(s1.equals(s2));
    }
}
