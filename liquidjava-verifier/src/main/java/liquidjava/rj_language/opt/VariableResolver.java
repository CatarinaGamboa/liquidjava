package liquidjava.rj_language.opt;

import java.util.HashMap;
import java.util.Map;
import liquidjava.rj_language.ast.BinaryExpression;
import liquidjava.rj_language.ast.Expression;
import liquidjava.rj_language.ast.Var;

public class VariableResolver {

    public static Map<String, Expression> resolve(Expression exp) {
        Map<String, Expression> map = new HashMap<>();
        resolveRecursive(exp, map);
        return resolveTransitive(map);
    }

    private static void resolveRecursive(Expression exp, Map<String, Expression> map) {
        if (!(exp instanceof BinaryExpression)) {
            return;
        }
        BinaryExpression be = (BinaryExpression) exp;
        String op = be.getOperator();
        if ("&&".equals(op)) {
            resolveRecursive(be.getFirstOperand(), map);
            resolveRecursive(be.getSecondOperand(), map);
        } else if ("==".equals(op)) {
            Expression left = be.getFirstOperand();
            Expression right = be.getSecondOperand();
            if (left instanceof Var && (right.isLiteral() || right instanceof Var)) {
                map.put(left.toString(), right.clone());
            } else if (right instanceof Var && left.isLiteral()) {
                map.put(right.toString(), left.clone());
            }
        }
    }

    // e.g. x == y && y == 1 => x == 1
    private static Map<String, Expression> resolveTransitive(Map<String, Expression> map) {
        Map<String, Expression> result = new HashMap<>();
        for (Map.Entry<String, Expression> entry : map.entrySet()) {
            result.put(entry.getKey(), lookup(entry.getValue(), map));
        }
        return result;
    }

    private static Expression lookup(Expression exp, Map<String, Expression> map) {
        if (!(exp instanceof Var)) {
            return exp;
        }
        Expression value = map.get(exp.toString());
        if (value == null) {
            return exp;
        }
        return lookup(value, map);
    }
}