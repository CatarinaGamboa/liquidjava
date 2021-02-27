package repair.regen.processor.constraints;

public class EqualsPredicate extends Predicate {
	
	public EqualsPredicate(Constraint c1, Constraint c2) {
		super("("+c1.toString()+" == "+ c2.toString()+")");
	}
	
	public EqualsPredicate(Constraint c1, String c2) {
		super("("+c1.toString()+" == "+ c2+")");
	}
	
	public EqualsPredicate(String variable, Constraint c) {
		super("("+variable+" == "+ c.toString()+")");
	}

	public EqualsPredicate(String variable, String assignment) {
		super("("+variable+" == "+ assignment+")");
	}

}
