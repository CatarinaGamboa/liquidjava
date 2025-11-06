import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import liquidjava.rj_language.ast.BinaryExpression;
import liquidjava.rj_language.ast.LiteralInt;
import liquidjava.rj_language.opt.ConstantFolding;
import liquidjava.rj_language.opt.derivation_node.ValDerivationNode;
import liquidjava.rj_language.ast.Var;

public class TestOptimizationInt {
    @Test
    public void testIntegerAddFold() {
        BinaryExpression b = new BinaryExpression(new LiteralInt(2), "+", new LiteralInt(3));

        ValDerivationNode r = ConstantFolding.fold(new ValDerivationNode(b, null));
        assertEquals(new LiteralInt(5), r.getValue());
    }

    @Test
    public void testNoFoldWhenNonLiteral() {
        BinaryExpression b = new BinaryExpression(new LiteralInt(1), "+", new Var("x"));
        ValDerivationNode r = ConstantFolding.fold(new ValDerivationNode(b, null));
        assertNotNull(r);
    }

    @Test
    public void testIntegerSubFold() {
        BinaryExpression b = new BinaryExpression(new LiteralInt(5), "-", new LiteralInt(2));
        ValDerivationNode r = ConstantFolding.fold(new ValDerivationNode(b, null));
        assertEquals(new LiteralInt(3), r.getValue());
    }

}