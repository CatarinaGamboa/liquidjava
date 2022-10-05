package repair.regen.processor.constraints;

import static org.junit.Assert.fail;

import repair.regen.rj_language.ast.BinaryExpression;

public class OperationPredicate extends Predicate {

    public OperationPredicate(Constraint c1, String op, Constraint c2) {
        super();
        if (c1 != null && c2 != null && op != null)
            exp = new BinaryExpression(c1.getExpression(), op, c2.getExpression());
        else
            fail("Something to implement later on Operation Predicate!");
    }

}
