package repair.regen.processor.constraints;

import java.util.Optional;

import repair.regen.language.BinaryExpression;
import repair.regen.language.Expression;
import repair.regen.language.parser.RefinementParser;

public class SingleConstraint extends Constraint{
	private Expression exp;
	
	public SingleConstraint(String ref) {
		Optional<Expression> oe = RefinementParser.parse(ref);
		if(oe.isPresent()) {
			Expression e = oe.get();
			exp = e;
		}else {
			//TODO throw Exception - not well formated
		}
	}
	public String toString() {
		return exp.toString();
	}
}
