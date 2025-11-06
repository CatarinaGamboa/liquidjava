package liquidjava.rj_language;

import static org.junit.Assert.*;
import org.junit.Test;
import liquidjava.rj_language.ast.LiteralString;

public class TestLiteralStringExtra {

    @Test
    public void testEqualStringsHaveEqualHashCodes() {
        LiteralString s1 = new LiteralString("hello");
        LiteralString s2 = new LiteralString("hello");
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    public void testDifferentStringsAreNotEqual() {
        LiteralString s1 = new LiteralString("hello");
        LiteralString s2 = new LiteralString("world");
        assertNotEquals(s1, s2);
    }
}
