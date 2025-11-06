package liquidjava.rj_language.ast;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
/**
*Testa se o método LiteralInt devolve o número em formato de texto
*/

class TestLiteralInt {

@Test
    void testToStringReturnsNumber() {
        assertEquals("42", new LiteralInt(42).toString());
    }
}
