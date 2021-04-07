package repair.regen.processor.constraints;

import static org.junit.Assert.fail;

public class OperationPredicate extends Predicate{
	
	public OperationPredicate(Constraint c1, String op, Constraint c2) {
		super();
		if(c1 != null && c2 != null && op != null)
			exp = String.format("((%s) %s (%s))", c1.getExpression(), op, c2.getExpression());
		else
			fail("Something to implement later on Operation Predicate!");
	}
	

}

