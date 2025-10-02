package liquidjava.rj_language.opt;

import liquidjava.rj_language.ast.Expression;

public class ExpressionSimplifier {

    public static Expression simplify(Expression exp) {
        System.out.println(exp);
        Expression current = simplifyExp(exp.clone());
        System.out.println(current);
        boolean changed = true;

        while (changed) {
            changed = false;

            Expression propagated = ConstantPropagation.propagate(current.clone());
            if (!propagated.equals(current)) {
                current = simplifyExp(propagated);
                System.out.println(current);
                changed = true;
                continue;
            }

            Expression folded = ConstantFolding.fold(current.clone());
            if (!folded.equals(current)) {
                current = simplifyExp(folded);
                System.out.println(current);
                changed = true;
                continue;
            }
        }
        return current;
    }

    private static Expression simplifyExp(Expression exp) {
        Expression current = exp.clone();
        while (true) {
            Expression simplified = LogicSimplifier.simplify(current.clone());
            if (simplified.equals(current)) {
                break;
            }
            current = simplified;
        }
        return current;
    }
}