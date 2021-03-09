package repair.regen.processor.constraints;

import java.util.ArrayList;
import java.util.List;

import repair.regen.language.Expression;
import repair.regen.language.ExpressionGroup;
import repair.regen.language.IfElseExpression;
import repair.regen.language.UnaryExpression;
import repair.regen.language.operators.NotOperator;

public class IfThenElse extends Constraint{
	private Constraint ite;
	
	public IfThenElse(Constraint a, Constraint b, Constraint c) {
		ite = new Predicate(new ExpressionGroup(new IfElseExpression(a.getExpression(), 
				b.getExpression(), c.getExpression())));
	}
	public IfThenElse(Constraint e) {
		ite = e;
	}

	@Override
	public Constraint substituteVariable(String from, String to) {
		Constraint i = ite.substituteVariable(from, to);
		return new IfThenElse(i);
	}

	@Override
	public Constraint negate() {
		return new Predicate(new UnaryExpression(new NotOperator(), ite.getExpression()));
	}

	@Override
	public Constraint clone() {
		return new IfThenElse(ite.clone());
	}

	@Override
	public List<String> getVariableNames() {
		List<String> a = new ArrayList<>();
		for(String s:ite.getVariableNames())
			if(!a.contains(s))
				a.add(s);
		return a;
	}

	@Override
	public String toString() {
		return ite.toString();
	}
	
	@Override
	public Expression getExpression() {
		return ite.getExpression();
	}
	@Override
	public boolean isBooleanTrue() {
		return false;
	}

}
