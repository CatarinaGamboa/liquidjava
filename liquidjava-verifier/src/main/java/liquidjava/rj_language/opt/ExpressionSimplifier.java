package liquidjava.rj_language.opt;

import liquidjava.rj_language.ast.BinaryExpression;
import liquidjava.rj_language.ast.Expression;
import liquidjava.rj_language.ast.LiteralBoolean;
import liquidjava.rj_language.opt.derivation_node.BinaryDerivationNode;
import liquidjava.rj_language.opt.derivation_node.DerivationNode;
import liquidjava.rj_language.opt.derivation_node.ValDerivationNode;

public class ExpressionSimplifier {

    /**
     * Simplifies an expression by applying constant propagation, constant folding and removing redundant conjuncts
     * Returns a derivation node representing the tree of simplifications applied
     */
    public static ValDerivationNode simplify(Expression exp) {
        ValDerivationNode prop = ConstantPropagation.propagate(exp);
        ValDerivationNode fold = ConstantFolding.fold(prop);
        return simplifyDerivationTree(fold);
    }

    /**
     * Recursively simplifies the derivation tree by removing redundant conjuncts
     */
    private static ValDerivationNode simplifyDerivationTree(ValDerivationNode node) {
        Expression value = node.getValue();
        DerivationNode origin = node.getOrigin();

        // binary expression with &&
        if (value instanceof BinaryExpression binExp) {
            if ("&&".equals(binExp.getOperator()) && origin instanceof BinaryDerivationNode binOrigin) {
                // recursively simplify children
                ValDerivationNode leftSimplified = simplifyDerivationTree(binOrigin.getLeft());
                ValDerivationNode rightSimplified = simplifyDerivationTree(binOrigin.getRight());

                // check if either side is redundant
                if (isRedundant(leftSimplified.getValue()))
                    return rightSimplified;
                if (isRedundant(rightSimplified.getValue()))
                    return leftSimplified;

                // return the conjunction with simplified children
                Expression newValue = new BinaryExpression(leftSimplified.getValue(), "&&", rightSimplified.getValue());
                DerivationNode newOrigin = new BinaryDerivationNode(leftSimplified, rightSimplified, "&&");
                return new ValDerivationNode(newValue, newOrigin);
            }
        }
        // no simplification
        return node;
    }

    /**
     * Checks if an expression is redundant (e.g. true or x == x)
     */
    private static boolean isRedundant(Expression exp) {
        // true
        if (exp instanceof LiteralBoolean && exp.isBooleanTrue()) {
            return true;
        }
        // x == x
        if (exp instanceof BinaryExpression binExp) {
            if ("==".equals(binExp.getOperator())) {
                Expression left = binExp.getFirstOperand();
                Expression right = binExp.getSecondOperand();
                return left.equals(right);
            }
        }
        return false;
    }
}
