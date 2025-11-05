package liquidjava.ast.opt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import liquidjava.rj_language.ast.BinaryExpression;
import liquidjava.rj_language.ast.LiteralBoolean;
import liquidjava.rj_language.ast.LiteralInt;
import liquidjava.rj_language.opt.ConstantFolding;
import liquidjava.rj_language.opt.derivation_node.ValDerivationNode;

public class TestOptimization {
    @Test
    public void testBinaryFold() {
        BinaryExpression b = new BinaryExpression(new LiteralInt(1), "+", new LiteralInt(2));

        ValDerivationNode r = ConstantFolding.fold(new ValDerivationNode(b, null));
        assertEquals(r.getValue(), new LiteralInt(3));
    }

    @Test
    public void testBinaryFoldBool() {
        // Given: true != false
        // Expected: true
        BinaryExpression b = new BinaryExpression(new LiteralBoolean(true), "!=", new LiteralBoolean(false));

        ValDerivationNode r = ConstantFolding.fold(new ValDerivationNode(b, null));

        // Then:
        assertEquals(r.getValue(), new LiteralBoolean(true), "Expect result to be true");
    }
}
