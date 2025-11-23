package liquidjava.rj_language.opt;

import liquidjava.rj_language.ast.BinaryExpression;
import liquidjava.rj_language.ast.Expression;
import liquidjava.rj_language.ast.UnaryExpression;
import liquidjava.rj_language.ast.Var;
import liquidjava.rj_language.opt.derivation_node.BinaryDerivationNode;
import liquidjava.rj_language.opt.derivation_node.DerivationNode;
import liquidjava.rj_language.opt.derivation_node.UnaryDerivationNode;
import liquidjava.rj_language.opt.derivation_node.ValDerivationNode;
import liquidjava.rj_language.opt.derivation_node.VarDerivationNode;

import java.util.Map;

public class ConstantPropagation {

    /**
     * Performs constant propagation on an expression, by substituting variables with their constant values. Uses the
     * VariableResolver to extract variable equalities from the expression first. Returns a derivation node representing
     * the propagation steps taken.
     */
    public static ValDerivationNode propagate(Expression exp, DerivationNode defaultOrigin) {
        Map<String, Expression> substitutions = VariableResolver.resolve(exp);
        return propagateRecursive(exp, substitutions, defaultOrigin);
    }

    /**
     * Recursively performs constant propagation on an expression (e.g. x + y && x == 1 && y == 2 => 1 + 2)
     */
    private static ValDerivationNode propagateRecursive(Expression exp, Map<String, Expression> subs,
            DerivationNode defaultOrigin) {

        // substitute variable
        if (exp instanceof Var var) {
            String name = var.getName();
            Expression value = subs.get(name);
            // substitution
            if (value != null)
                return new ValDerivationNode(value.clone(), new VarDerivationNode(name));

            // no substitution
            return new ValDerivationNode(var, defaultOrigin);
        }

        // lift unary origin
        if (exp instanceof UnaryExpression unary) {
            ValDerivationNode operand = propagateRecursive(unary.getChildren().get(0), subs, defaultOrigin);
            UnaryExpression cloned = (UnaryExpression) unary.clone();
            cloned.setChild(0, operand.getValue());

            DerivationNode origin = operand.getOrigin() != null ? new UnaryDerivationNode(operand, cloned.getOp())
                    : defaultOrigin;
            return new ValDerivationNode(cloned, origin);
        }

        // lift binary origin
        if (exp instanceof BinaryExpression binary) {
            ValDerivationNode left = propagateRecursive(binary.getFirstOperand(), subs, defaultOrigin);
            ValDerivationNode right = propagateRecursive(binary.getSecondOperand(), subs, defaultOrigin);
            BinaryExpression cloned = (BinaryExpression) binary.clone();
            cloned.setChild(0, left.getValue());
            cloned.setChild(1, right.getValue());

            DerivationNode origin = (left.getOrigin() != null || right.getOrigin() != null)
                    ? new BinaryDerivationNode(left, right, cloned.getOperator()) : defaultOrigin;
            return new ValDerivationNode(cloned, origin);
        }

        // recursively propagate children
        if (exp.hasChildren()) {
            Expression propagated = exp.clone();
            for (int i = 0; i < exp.getChildren().size(); i++) {
                ValDerivationNode child = propagateRecursive(exp.getChildren().get(i), subs, defaultOrigin);
                propagated.setChild(i, child.getValue());
            }
            return new ValDerivationNode(propagated, defaultOrigin);
        }

        // no propagation
        return new ValDerivationNode(exp, defaultOrigin);
    }
}