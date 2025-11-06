package liquidjava.rj_language.ast;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LiteralBooleanSimplestTest {

    @Test
    void testIsBooleanTrue() {
        assertTrue(new LiteralBoolean(true).isBooleanTrue());
    }
}
