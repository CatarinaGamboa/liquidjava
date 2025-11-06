package liquidjava.rj_language.ast;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestLiteralInt {

    @Test
    void testToStringReturnsNumber() {
        assertEquals("42", new LiteralInt(42).toString());
    }
}
