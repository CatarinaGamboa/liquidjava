package repair.regen.processor.constraints;

public class LiteralPredicate {
	
	public static Constraint getIntPredicate(int i) {
		return new Predicate(Integer.toString(i));
	}

	//TODO COMPLETE
}
