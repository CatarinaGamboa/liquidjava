
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

    @Test
    public void testEqualsSameContent() {
        LiteralString a = new LiteralString("hello");
        LiteralString b = new LiteralString("hello");
        assertTrue(a.equals(b));
        assertTrue(b.equals(a));
    }

    @Test
    public void testEqualsDifferentContent() {
        LiteralString a = new LiteralString("hello");
        LiteralString c = new LiteralString("world");
        assertFalse(a.equals(c));
        assertFalse(c.equals(a));
    }

    @Test
    public void testEqualsSameReference() {
        LiteralString a = new LiteralString("hello");
        assertTrue(a.equals(a));
    }

    @Test
    public void testEqualsNull() {
        LiteralString a = new LiteralString("hello");
        assertFalse(a.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        LiteralString a = new LiteralString("hello");
        assertFalse(a.equals(new Object()));
    }
}
