package liquidjava.rj_language;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import liquidjava.rj_language.ast.LiteralInt;
import liquidjava.rj_language.ast.BinaryExpression;

public class TesteInteracao {
    @Test
    public void testBinaryExpressionLiteralInt() {

        BinaryExpression n1 = new BinaryExpression(new LiteralInt(1), "+", new LiteralInt(2));

        BinaryExpression n2 = new BinaryExpression(n1, "*", new LiteralInt(3));

        assertEquals("(1 + 2) * 3", n2.toString());

    }
}