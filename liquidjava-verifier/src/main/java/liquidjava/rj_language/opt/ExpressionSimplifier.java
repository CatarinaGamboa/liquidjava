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
        ValDerivationNode fixedPoint = simplifyToFixedPoint(null, exp);
        return simplifyValDerivationNode(fixedPoint);
    }

    /**
     * Recursively applies propagation and folding until the expression stops changing (fixed point) Stops early if the
     * expression simplifies to 'true', which means we've simplified too much
     */
    private static ValDerivationNode simplifyToFixedPoint(ValDerivationNode current, Expression prevExp) {
        // apply propagation and folding
        ValDerivationNode prop = ConstantPropagation.propagate(prevExp, current);
        ValDerivationNode fold = ConstantFolding.fold(prop);
        ValDerivationNode simplified = simplifyValDerivationNode(fold);
        Expression currExp = simplified.getValue();

        // fixed point reached
        if (current != null && currExp.equals(current.getValue())) {
            return current;
        }

        // continue simplifying
        return simplifyToFixedPoint(simplified, simplified.getValue());
    }

    /**
     * Recursively simplifies the derivation tree by removing redundant conjuncts
     */
    private static ValDerivationNode simplifyValDerivationNode(ValDerivationNode node) {
        Expression value = node.getValue();
        DerivationNode origin = node.getOrigin();

        // binary expression with &&
        if (value instanceof BinaryExpression binExp && "&&".equals(binExp.getOperator())) {
            ValDerivationNode leftSimplified;
            ValDerivationNode rightSimplified;

            if (origin instanceof BinaryDerivationNode binOrigin) {
                leftSimplified = simplifyValDerivationNode(binOrigin.getLeft());
                rightSimplified = simplifyValDerivationNode(binOrigin.getRight());
            } else {
                leftSimplified = simplifyValDerivationNode(new ValDerivationNode(binExp.getFirstOperand(), null));
                rightSimplified = simplifyValDerivationNode(new ValDerivationNode(binExp.getSecondOperand(), null));
            }

            // check if either side is redundant
            if (isRedundant(leftSimplified.getValue()))
                return rightSimplified;
            if (isRedundant(rightSimplified.getValue()))
                return leftSimplified;

            // collapse identical sides (x && x => x)
            if (leftSimplified.getValue().toString().equals(rightSimplified.getValue().toString())) {
                return leftSimplified;
            }

            // return the conjunction with simplified children
            Expression newValue = new BinaryExpression(leftSimplified.getValue(), "&&", rightSimplified.getValue());
            DerivationNode newOrigin = new BinaryDerivationNode(leftSimplified, rightSimplified, "&&");
            return new ValDerivationNode(newValue, newOrigin);
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
