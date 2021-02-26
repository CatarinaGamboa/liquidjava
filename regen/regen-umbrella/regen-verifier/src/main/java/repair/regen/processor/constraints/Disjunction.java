package repair.regen.processor.constraints;

import java.util.List;

public class Disjunction extends Constraint{
	
	private Constraint c1;
	private Constraint c2;

	private Disjunction(Constraint c1, Constraint c2) {
		this.c1 = c1;
		this.c2 = c2;
	}
	
	public static Constraint createDisjunction(Constraint c1, Constraint c2) {
		if(!isLitTrue(c1) && !isLitTrue(c2))
			return new Disjunction(c1, c2);
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
		return new Disjunction(nc1, nc2);
	}

	@Override
	public Constraint negate() {
		Predicate p = new Predicate(this.toString());
		return p.negate();
	}

	@Override
	public Constraint clone() {
		return new Disjunction(c1.clone(), c2.clone());
	}

	@Override
	public List<String> getVariableNames() {
		List<String> l1 = c1.getVariableNames();
		l1.addAll(c2.getVariableNames());
		return l1;
	}

	@Override
	public String toString() {
		return "("+c1.toString() + " || " + c2.toString()+")";
	}


}
