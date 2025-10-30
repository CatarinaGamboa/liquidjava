package liquidjava.ast.opt;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import liquidjava.rj_language.ast.BinaryExpression;
import liquidjava.rj_language.ast.LiteralInt;
import liquidjava.rj_language.opt.ConstantFolding;
import liquidjava.rj_language.opt.derivation_node.ValDerivationNode;

public class TestOptimizationIntegration {
    @Test
    public void testBinaryFoldNested() {
        BinaryExpression inner = new BinaryExpression(new LiteralInt(2), "+", new LiteralInt(3));
        BinaryExpression outer = new BinaryExpression(new LiteralInt(1), "+", inner);
        ValDerivationNode r = ConstantFolding.fold(new ValDerivationNode(outer, null));
        assertEquals(new LiteralInt(6), r.getValue());
    }
}

// António Rebelo - 58530