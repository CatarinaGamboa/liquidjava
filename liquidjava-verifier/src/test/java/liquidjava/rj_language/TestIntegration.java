package liquidjava.integration;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import liquidjava.rj_language.ast.BinaryExpression;
import liquidjava.rj_language.ast.LiteralInt;
import liquidjava.rj_language.opt.ConstantFolding;
import liquidjava.rj_language.opt.derivation_node.ValDerivationNode;

public class TestIntegration {
    @Test
    public void testConstantFoldingIntegration() {
        BinaryExpression expr = new BinaryExpression(new LiteralInt(3), "*", new LiteralInt(4));
        ValDerivationNode result = ConstantFolding.fold(new ValDerivationNode(expr, null));
        assertEquals(new LiteralInt(12), result.getValue());
    }
}
