package liquidjava.rj_language.opt;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import liquidjava.rj_language.ast.BinaryExpression;
import liquidjava.rj_language.ast.Expression;
import liquidjava.rj_language.ast.Var;

public class VariableResolver {

    /**
     * Extracts variables with constant values from an expression Returns a map from variable names to their values
     */
    public static Map<String, Expression> resolve(Expression exp) {
        // if the expression is just a single equality (not a conjunction) don't extract it
        // this avoids creating tautologies like "1 == 1" after substitution, which are then simplified to "true"
        if (exp instanceof BinaryExpression) {
            BinaryExpression be = (BinaryExpression) exp;
            if ("==".equals(be.getOperator())) {
                return new HashMap<>();
            }
        }

        Map<String, Expression> map = new HashMap<>();
        resolveRecursive(exp, map);
        return resolveTransitive(map);
    }

    /**
     * Recursively extracts variable equalities from an expression (e.g. ... && x == 1 && y == 2 => map: x -> 1, y -> 2)
     * Modifies the given map in place
     */
    private static void resolveRecursive(Expression exp, Map<String, Expression> map) {
        if (!(exp instanceof BinaryExpression))
            return;

        BinaryExpression be = (BinaryExpression) exp;
        String op = be.getOperator();
        if ("&&".equals(op)) {
            resolveRecursive(be.getFirstOperand(), map);
            resolveRecursive(be.getSecondOperand(), map);
        } else if ("==".equals(op)) {
            Expression left = be.getFirstOperand();
            Expression right = be.getSecondOperand();
            if (left instanceof Var && (right.isLiteral() || right instanceof Var)) {
                map.put(((Var) left).getName(), right.clone());
            } else if (right instanceof Var && left.isLiteral()) {
                map.put(((Var) right).getName(), left.clone());
            }
        }
    }

    /**
     * Handles transitive variable equalities in the map (e.g. map: x -> y, y -> 1 => map: x -> 1, y -> 1)
     */
    private static Map<String, Expression> resolveTransitive(Map<String, Expression> map) {
        Map<String, Expression> result = new HashMap<>();
        for (Map.Entry<String, Expression> entry : map.entrySet()) {
            result.put(entry.getKey(), lookup(entry.getValue(), map, new HashSet<>()));
        }
        return result;
    }

    /**
     * Returns the value of a variable by looking up in the map recursively Uses the seen set to avoid circular
     * references (e.g. x -> y, y -> x) which would cause infinite recursion
     */
    private static Expression lookup(Expression exp, Map<String, Expression> map, Set<String> seen) {
        if (!(exp instanceof Var))
            return exp;

        String name = exp.toString();
        if (seen.contains(name))
            return exp; // circular reference

        Expression value = map.get(name);
        if (value == null)
            return exp;

        seen.add(name);
        return lookup(value, map, seen);
    }
}