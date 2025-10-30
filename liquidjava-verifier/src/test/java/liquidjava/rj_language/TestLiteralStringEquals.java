package liquidjava.rj_language;

import static org.junit.Assert.*;
import org.junit.Test;
import liquidjava.rj_language.ast.LiteralString;

public class TestLiteralStringEquals {
    @Test
    public void testBothValuesNull() {
        LiteralString a = new LiteralString(null);
        LiteralString b = new LiteralString(null);
        assertTrue(a.equals(b));
        assertTrue(b.equals(a));
        assertTrue(a.equals(a));
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void testThisValueNullOtherNotNull() {
        LiteralString a = new LiteralString(null);
        LiteralString b = new LiteralString("hello");
        assertFalse(a.equals(b));
        assertFalse(b.equals(a));
    }

    @Test
    public void testOtherValueNullThisNotNull() {
        LiteralString a = new LiteralString("hello");
        LiteralString b = new LiteralString(null);
        assertFalse(a.equals(b));
        assertFalse(b.equals(a));
    }
}