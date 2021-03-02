package repair.regen.processor.constraints;

import repair.regen.language.Variable;

public class VariablePredicate extends Predicate{
	public VariablePredicate(String name) {
		super();
		setExpression(new Variable(name));
	}

}
