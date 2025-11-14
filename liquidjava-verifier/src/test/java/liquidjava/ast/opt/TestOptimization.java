package liquidjava.ast.opt;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import liquidjava.rj_language.ast.BinaryExpression;
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
    public void testBinaryExpressionWithLiteralInt() {
        LiteralInt numero5 = new LiteralInt(5);
        LiteralInt numero3 = new LiteralInt(3);

        BinaryExpression expressao = new BinaryExpression(numero5, "+", numero3);

        String resultado = expressao.toString();

        assertEquals("5 + 3", resultado);
    }
}
