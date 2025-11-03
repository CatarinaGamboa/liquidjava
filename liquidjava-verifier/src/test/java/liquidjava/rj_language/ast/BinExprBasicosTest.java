package liquidjava.rj_language.ast;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BinaryExpressionBasicsTest {

    @Test
    void testeOperacaoAritmetica() {
        BinaryExpression be = new BinaryExpression(new LiteralInt(1), "+", new LiteralInt(2));

        assertTrue(be.isArithmeticOperation());
        assertFalse(be.isBooleanOperation());
        assertFalse(be.isLogicOperation());

        assertEquals("1 + 2", be.toString());
    }
}