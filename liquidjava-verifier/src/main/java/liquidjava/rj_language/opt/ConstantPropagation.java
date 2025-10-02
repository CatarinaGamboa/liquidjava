package liquidjava.rj_language.opt;

import liquidjava.rj_language.ast.Expression;
import liquidjava.rj_language.ast.Var;
import java.util.Map;

public class ConstantPropagation {

    public static Expression propagate(Expression exp) {
        Map<String, Expression> substitutions = AssertionExtractor.extract(exp);
        return propagateRecursive(exp, substitutions);
    }

    private static Expression propagateRecursive(Expression exp, Map<String, Expression> substitutions) {
        // apply substitutions to children for other expression types
        if (exp.hasChildren()) {
            Expression result = exp.clone();
            for (int i = 0; i < result.getChildren().size(); i++) {
                Expression child = result.getChildren().get(i);
                Expression substitutedChild = propagateRecursive(child, substitutions);
                result.setChild(i, substitutedChild);
            }
            return result;
        }
        // if the expression is a variable, substitute if in map
        if (exp instanceof Var) {
            Var var = (Var) exp;
            String varName = var.getName();
            if (substitutions.containsKey(varName)) {
                return substitutions.get(varName).clone();
            }
        }
        return exp;
    }
}
