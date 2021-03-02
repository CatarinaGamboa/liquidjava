package repair.regen.processor.constraints;

import repair.regen.language.BinaryExpression;
import repair.regen.language.Expression;
import repair.regen.language.ExpressionGroup;
import repair.regen.language.operators.EqualsOperator;

public class EqualsPredicate extends Predicate {
	
	public EqualsPredicate(Constraint c1, Constraint c2) {
		super();
		setExpression(getEqualsExpressionGroup(c1.getExpression(),c2.getExpression()));		
	}


	public EqualsPredicate(Constraint c1, String c2) {
		super();
		setExpression(getEqualsExpressionGroup(c1.getExpression(),parse(c2)));
	}
	
//	public EqualsPredicate(String variable, Constraint c) {
//		super();
//		setExpression(getEqualsExpressionGroup(parse(variable),c.getExpression()));
//	}
//
//	public EqualsPredicate(String variable, String assignment) {
//		super();
//		setExpression(getEqualsExpressionGroup(parse(variable), parse(assignment)));
//	}
	
	
	private Expression getEqualsExpressionGroup(Expression expression,
			Expression expression2) {
		return new ExpressionGroup(new BinaryExpression(expression, new EqualsOperator(), expression2));
	}

}
