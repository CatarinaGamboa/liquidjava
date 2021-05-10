package repair.regen.processor.constraints;

import java.util.List;

import repair.regen.ast.BinaryExpression;
import repair.regen.ast.Expression;
import repair.regen.ast.GroupExpression;
import repair.regen.ast.UnaryExpression;
import repair.regen.processor.context.GhostState;
import repair.regen.utils.ErrorEmitter;

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
		return String.format("((%s) && (%s))", c1.getExpression().toString(), 
											   c2.getExpression().toString());
	}

	@Override
	public Expression getExpression() {
		return new GroupExpression(new BinaryExpression(c1.getExpression(), "&&", c2.getExpression()));
	}
	
	@Override
	public boolean isBooleanTrue() {
		return c1.isBooleanTrue() && c2.isBooleanTrue();
	}


	@Override
	public Constraint changeOldMentions(String previousName, String newName, ErrorEmitter ee) {
		Constraint c1_ = c1.changeOldMentions(previousName, newName, ee);
		Constraint c2_ = c2.changeOldMentions(previousName, newName, ee);
		return new Conjunction(c1_, c2_);
	}

	@Override
	public Constraint changeStatesToRefinements(List<GhostState> ghostState, String[] toChange, ErrorEmitter ee)  {
		return new Conjunction(c1.changeStatesToRefinements(ghostState, toChange, ee), 
				c2.changeStatesToRefinements(ghostState, toChange, ee));
	}
	

}
