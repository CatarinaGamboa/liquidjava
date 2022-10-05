package repair.regen.processor.constraints;

import repair.regen.errors.ErrorEmitter;
import repair.regen.rj_language.ast.BinaryExpression;
import repair.regen.rj_language.ast.GroupExpression;

public class EqualsPredicate extends Predicate {

    public EqualsPredicate(Constraint c1, Constraint c2) {
        super();
        exp = new GroupExpression(new BinaryExpression(c1.getExpression(), "==", c2.getExpression()));
    }

    public EqualsPredicate(Constraint c1, String c2, ErrorEmitter ee) {
        super();
        exp = new GroupExpression(new BinaryExpression(c1.getExpression(), "==", innerParse(c2, ee)));
    }

}
