package repair.regen.processor.constraints;

public class EqualsPredicate extends Predicate {
	
	public EqualsPredicate(String variable, Constraint c) {
		super("("+variable+" == "+ c.toString()+")");
	}
	
	public EqualsPredicate(String variable, String assignment) {
		super("("+variable+" == "+ assignment+")");
	}

}
