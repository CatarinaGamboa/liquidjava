package liquidjava.rj_language.opt;

import java.util.HashMap;
import java.util.Map;

import liquidjava.rj_language.ast.BinaryExpression;
import liquidjava.rj_language.ast.Expression;
import liquidjava.rj_language.ast.LiteralBoolean;
import liquidjava.rj_language.ast.LiteralInt;
import liquidjava.rj_language.ast.LiteralReal;
import liquidjava.rj_language.ast.Var;

public class VariableCollector {

    public static Map<String, Expression> collect(Expression exp) {
        Map<String, Expression> assertions = new HashMap<>();

        // only extract assertions if the expression contains conjunctions (&&)
        if (containsConjunction(exp)) {
            collectRecursive(exp, assertions);
        }
        return assertions;
    }

    private static boolean containsConjunction(Expression exp) {
        if (exp instanceof BinaryExpression) {
            BinaryExpression binExp = (BinaryExpression) exp;
            if (binExp.getOperator().equals("&&")) {
                return true;
            }
            // recursively check children
            return containsConjunction(binExp.getFirstOperand()) || containsConjunction(binExp.getSecondOperand());
        }
        if (exp.hasChildren()) {
            for (Expression child : exp.getChildren()) {
                if (containsConjunction(child)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void collectRecursive(Expression exp, Map<String, Expression> assertions) {
        if (exp instanceof BinaryExpression) {
            BinaryExpression binExp = (BinaryExpression) exp;
            String operator = binExp.getOperator();

            if (operator.equals("&&")) {
                // for conjunctions recursively extract from both sides
                collectRecursive(binExp.getFirstOperand(), assertions);
                collectRecursive(binExp.getSecondOperand(), assertions);
            } else if (operator.equals("==")) {
                // for assertions check if one side is a variable and the other is a literal
                Expression left = binExp.getFirstOperand();
                Expression right = binExp.getSecondOperand();
                if (left instanceof Var && isLiteral(right)) {
                    assertions.put(((Var) left).getName(), right);
                } else if (right instanceof Var && isLiteral(left)) {
                    assertions.put(((Var) right).getName(), left);
                }
            }
        }
        // for other expressions, recurse into children
        else if (exp.hasChildren()) {
            for (Expression child : exp.getChildren()) {
                collectRecursive(child, assertions);
            }
        }
    }

    private static boolean isLiteral(Expression exp) {
        return exp instanceof LiteralInt || exp instanceof LiteralReal || exp instanceof LiteralBoolean;
    }
}
