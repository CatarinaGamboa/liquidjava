package repair.regen.processor.constraints;

import java.util.ArrayList;
import java.util.List;

import repair.regen.processor.context.GhostState;

public class IfThenElse extends Constraint{
	private Constraint ite;
	
	public IfThenElse(Constraint a, Constraint b, Constraint c) {
		String s = String.format("((%s) ? (%s) : (%s))", a, b, c);
		ite = new Predicate(s);
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
		return new Predicate(String.format("!(%s)", getExpression()));
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
	public String getExpression() {
		return ite.getExpression();
	}
	@Override
	public boolean isBooleanTrue() {
		return false;
	}
	@Override
	public Constraint changeOldMentions(String previousName, String newName) {
		return ite.changeOldMentions(previousName, newName);
	}
	
	@Override
	public Constraint changeStatesToRefinements(List<GhostState> ghostState, String[] ls) {
		return ite.changeStatesToRefinements(ghostState, ls);
	}

}
