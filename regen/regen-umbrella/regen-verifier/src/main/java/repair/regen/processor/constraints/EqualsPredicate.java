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
				new BinaryExpression(c1.getExpression(), "==", parse(c2)));
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
//	
//	
//	private Expression getEqualsExpressionGroup(Expression expression,
//			Expression expression2) {
//		return new ExpressionGroup(new BinaryExpression(expression, new EqualsOperator(), expression2));
//	}

}
