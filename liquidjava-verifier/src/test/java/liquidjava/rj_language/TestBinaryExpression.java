package liquidjava.rj_language;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import liquidjava.rj_language.ast.BinaryExpression;
import liquidjava.rj_language.ast.LiteralInt;
import liquidjava.rj_language.opt.ConstantFolding;
import liquidjava.rj_language.opt.derivation_node.ValDerivationNode;

public class TestBinaryExpression {
    @Test
    public void testSimpleAddition() {
        BinaryExpression expr = new BinaryExpression(new LiteralInt(5), "+", new LiteralInt(7));
        ValDerivationNode result = ConstantFolding.fold(new ValDerivationNode(expr, null));
        assertEquals(new LiteralInt(12), result.getValue());
    }
}