package liquidjava.rj_language.ast;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
*Testa o método LiteralBoolean devolve a string "true" quando o valor booleano interno é verdadeiro
*/

class LiteralBooleanSimplestTest {

    @Test
    void testIsBooleanTrue() {
        assertTrue(new LiteralBoolean(true).isBooleanTrue());
    }
}
