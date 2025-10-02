package liquidjava.rj_language.opt;

import liquidjava.rj_language.ast.Expression;

public class ExpressionSimplifier {

    public static DerivationNode simplify(Expression exp) {
        DerivationNode currentNode = new DerivationNode(exp);
        Expression currentExp = simplifyExp(exp.clone());
        currentNode = currentNode.addNode(currentExp);
        boolean changed = true;

        while (changed) {
            changed = false;

            Expression propagated = ConstantPropagation.propagate(currentExp.clone());
            if (!propagated.equals(currentExp)) {
                currentExp = simplifyExp(propagated);
                currentNode = currentNode.addNode(currentExp);
                changed = true;
                continue;
            }

            Expression folded = ConstantFolding.fold(currentExp.clone());
            if (!folded.equals(currentExp)) {
                currentExp = simplifyExp(folded);
                currentNode = currentNode.addNode(currentExp);
                System.out.println(currentExp);
                changed = true;
                continue;
            }
        }
        return currentNode;
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