package repair.regen.processor.constraints;

import java.util.List;

public class Conjunction extends Constraint{
	private Constraint c1;
	private Constraint c2;

	public Conjunction(Constraint c1, Constraint c2) {
		this.c1 = c1;
		this.c2 = c2;
	}
	
	@Override
	public Constraint substituteVariable(String from, String to) {
		Constraint nc1 = c1.substituteVariable(from, to);
		Constraint nc2 = c2.substituteVariable(from, to);
		return new Conjunction(nc1, nc2);
	}

	@Override
	public Constraint negate() {
		Predicate p = new Predicate(this.toString());
		return p.negate();
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

}
