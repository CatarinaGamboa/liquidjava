package liquidjava.rj_language.opt;

import java.util.ArrayList;
import java.util.List;
import liquidjava.rj_language.ast.Expression;

public class ExpressionSimplifier {

    public static List<Expression> simplify(Expression exp) {
        ArrayList<Expression> derivationSteps = new ArrayList<>();
        Expression current = exp.clone();
        boolean changed = true;

        derivationSteps.add(current);
        while (changed) {
            changed = false;

            Expression simplified = LogicSimplifier.simplify(current.clone());
            if (!simplified.equals(current)) {
                // derivationSteps.add(simplified);
                current = simplified;
                changed = true;
                continue;
            }

            Expression propagated = ConstantPropagation.propagate(current.clone());
            if (!propagated.equals(current)) {
                // derivationSteps.add(propagated);
                current = propagated;
                changed = true;
                continue;
            }

            Expression folded = ConstantFolding.fold(current.clone());
            if (!folded.equals(current)) {
                derivationSteps.add(folded);
                current = folded;
                changed = true;
                continue;
            }
        }
        return derivationSteps;
    }
}