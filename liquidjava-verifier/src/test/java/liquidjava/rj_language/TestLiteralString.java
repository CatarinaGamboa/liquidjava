
package liquidjava.rj_language;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    @Test
    public void testLiteralStringEqualsClass() {
        // Given: s1.getClass() == LiteralString && s2.getClass() == String
        // Expected: False (LiteralString != String)

        LiteralString s1 = new LiteralString("hello");
        String s2 = "hello";

        // when
        boolean result = s1.equals(s2);

        // Then:
        assertFalse(result, "Expected result to be False");
    }

    @Test
    public void testLiteralStringEqualsNull() {
        // Given: s1 == null && s2 == null
        // Expected: True (null == null)

        LiteralString s1 = new LiteralString(null);
        LiteralString s2 = new LiteralString(null);

        // When
        boolean result = s1.equals(s2);

        // Then
        assertTrue(result, "Expected result to be True");
    }

}
