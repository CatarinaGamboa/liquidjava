package repair.regen.processor.constraints;

import repair.regen.ast.BinaryExpression;
import repair.regen.ast.GroupExpression;

public class EqualsPredicate extends Predicate {
	
	public EqualsPredicate(Constraint c1, Constraint c2) {
		super();
		exp = new GroupExpression(
				new BinaryExpression(c1.getExpression(), "==", c2.getExpression()));
	}


	public EqualsPredicate(Constraint c1, String c2) {
		super();
		exp = new GroupExpression(
				new BinaryExpression(c1.getExpression(), "==", innerParse(c2)));
	}
	
}
