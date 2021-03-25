package repair.regen.processor.constraints;

import java.util.List;

import repair.regen.language.BinaryExpression;
import repair.regen.language.Expression;
import repair.regen.language.ExpressionGroup;
import repair.regen.language.UnaryExpression;
import repair.regen.language.operators.AndOperator;
import repair.regen.language.operators.NotOperator;
import repair.regen.processor.context.GhostState;

public class Conjunction extends Constraint{
	private Constraint c1;
	private Constraint c2;

	private Conjunction(Constraint c1, Constraint c2) {
		this.c1 = c1;
		this.c2 = c2;
	}
	
	public static Constraint createConjunction(Constraint c1, Constraint c2) {
		if(!isLitTrue(c1) && !isLitTrue(c2))
			return new Conjunction(c1, c2);
		if(isLitTrue(c1))
			return c2;
		return c1;
	}
	
	private static boolean isLitTrue(Constraint c) {
		return c instanceof Predicate && ((Predicate)c).isBooleanTrue();
	}

	@Override
	public Constraint substituteVariable(String from, String to) {
		Constraint nc1 = c1.substituteVariable(from, to);
		Constraint nc2 = c2.substituteVariable(from, to);
		return new Conjunction(nc1, nc2);
	}

	@Override
	public Constraint negate() {
		return new Predicate(new UnaryExpression(new NotOperator(), getExpression()));
	}

	@Override
	public Constraint clone() {
		return new Conjunction(c1.clone(), c2.clone());
	}

	@Override
	public List<String> getVariableNames() {
		List<String> l1 = c1.getVariableNames();
		l1.addAll(c2.getVariableNames());
		return l1;
	}

	@Override
	public String toString() {
		return "("+c1.toString() + " && " + c2.toString()+")";
	}

	@Override
	public Expression getExpression() {
		return new ExpressionGroup(new BinaryExpression(c1.getExpression(), new AndOperator(), c2.getExpression()));
	}
	
	@Override
	public boolean isBooleanTrue() {
		return c1.isBooleanTrue() && c2.isBooleanTrue();
	}


	@Override
	public Constraint changeOldMentions(String previousName, String newName) {
		Constraint c1_ = c1.changeOldMentions(previousName, newName);
		Constraint c2_ = c2.changeOldMentions(previousName, newName);
		return new Conjunction(c1_, c2_);
	}

	@Override
	public Constraint changeStatesToRefinements(List<GhostState> ghostState) {
		return new Conjunction(c1.changeStatesToRefinements(ghostState), c2.changeStatesToRefinements(ghostState));
	}

}
