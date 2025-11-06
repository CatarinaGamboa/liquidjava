package liquidjava.rj_language;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import liquidjava.rj_language.ast.LiteralInt;
import liquidjava.rj_language.ast.BinaryExpression;

public class TestBinaryExpression {
    @Test
    public void testBinaryExpression() {

        LiteralInt left = new LiteralInt(3);
        LiteralInt right = new LiteralInt(4);

        BinaryExpression expr = new BinaryExpression(left, "+", right);

        assertEquals("3 + 4", expr.toString());

    }
}