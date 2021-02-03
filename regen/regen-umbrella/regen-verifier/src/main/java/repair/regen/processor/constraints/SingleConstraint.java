package repair.regen.processor.constraints;

import java.util.Optional;

import org.modelcc.parser.ParserException;

import repair.regen.language.BinaryExpression;
import repair.regen.language.Expression;
import repair.regen.language.parser.RefinementParser;
import repair.regen.language.parser.SyntaxException;

public class SingleConstraint extends Constraint{
	private Expression exp;

	public SingleConstraint(String ref) {
		try{
			Optional<Expression> oe = RefinementParser.parse(ref);

			if(oe.isPresent()) {
				Expression e = oe.get();
				exp = e;
			}
		} catch (SyntaxException e1) {
			System.err.println("Refinement does not follow the language directives: "+ref);
			e1.printStackTrace();
		}
	}
	public String toString() {
		return exp.toString();
	}
}
