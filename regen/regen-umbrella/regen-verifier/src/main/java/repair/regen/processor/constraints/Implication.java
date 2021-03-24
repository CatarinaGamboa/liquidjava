package repair.regen.processor.constraints;

import java.util.List;

import repair.regen.language.BinaryExpression;

import repair.regen.language.Expression;
import repair.regen.language.ExpressionGroup;
import repair.regen.language.UnaryExpression;
import repair.regen.language.operators.ImpliesOperator;
import repair.regen.language.operators.NotOperator;

public class Implication extends Constraint{
	
	private Constraint c1;
	private Constraint c2;
	
	
	public Implication(Constraint c1, Constraint c2) {
		this.c1 = c1;
		this.c2 = c2;
	}

	@Override
	public Constraint substituteVariable(String from, String to) {
		return new Implication(c1.substituteVariable(from, to), c2.substituteVariable(from, to));
	}

	@Override
	public Constraint negate() {
		return new Predicate(new UnaryExpression(new NotOperator(), getExpression()));
	}

	@Override
	public Constraint clone() {
		return new Implication(c1.clone(), c2.clone());
	}

	@Override
	public List<String> getVariableNames() {
		List<String> l1 = c1.getVariableNames();
		l1.addAll(c2.getVariableNames());
		return l1;
	}

	@Override
	public String toString() {
		return  "("+c1.toString() + " --> " + c2.toString()+")";
	}

	@Override
	public Expression getExpression() {
		BinaryExpression be = new BinaryExpression(c1.getExpression(), 
				new ImpliesOperator(), c2.getExpression());
		return new ExpressionGroup(be);
	}

	@Override
	public boolean isBooleanTrue() {
		return c1.isBooleanTrue() && c2.isBooleanTrue();
	}

	@Override
	public Constraint changeOldMentions(String previousName, String newName) {
		Constraint c1_ = c1.changeOldMentions(previousName, newName);
		Constraint c2_ = c2.changeOldMentions(previousName, newName);
		return new Implication(c1_, c2_);
	}

}
