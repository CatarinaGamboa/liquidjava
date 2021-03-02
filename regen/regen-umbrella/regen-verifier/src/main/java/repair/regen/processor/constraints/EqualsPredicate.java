package repair.regen.processor.constraints;

import repair.regen.language.BinaryExpression;
import repair.regen.language.operators.EqualsOperator;

public class EqualsPredicate extends Predicate {
	
	public EqualsPredicate(Constraint c1, Constraint c2) {
		super();
		setExpression(new BinaryExpression(c1.getExpression(), new EqualsOperator(), c2.getExpression()));		
	}
	
	public EqualsPredicate(Constraint c1, String c2) {
		super();
		setExpression(new BinaryExpression(c1.getExpression(), new EqualsOperator(), parse(c2)));
	}
	
	public EqualsPredicate(String variable, Constraint c) {
		super();
		setExpression(new BinaryExpression(parse(variable), new EqualsOperator(), c.getExpression()));
	}

	public EqualsPredicate(String variable, String assignment) {
		super();
		setExpression(new BinaryExpression(parse(variable), new EqualsOperator(), parse(assignment)));
	}

}
