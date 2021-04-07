package repair.regen.processor.constraints;

import repair.regen.language.IntegerLiteral;

public class LiteralPredicate {
	
	public static Constraint getIntPredicate(int i) {
		return new Predicate(Integer.toString(i));
	}

	//TODO COMPLETE
}
