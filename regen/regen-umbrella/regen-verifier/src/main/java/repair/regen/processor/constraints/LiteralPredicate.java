package repair.regen.processor.constraints;

import repair.regen.language.IntegerLiteral;

public class LiteralPredicate {
	
	public static Constraint getIntPredicate(int i) {
		return new Predicate(new IntegerLiteral(i));
	}

	//TODO COMPLETE
}
