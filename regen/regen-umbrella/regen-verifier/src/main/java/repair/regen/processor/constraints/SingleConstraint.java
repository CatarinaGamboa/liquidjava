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
			if(e instanceof BinaryExpression) {
				BinaryExpression be = (BinaryExpression) e;
				System.out.println("Here:"+be.toString());
			}
			System.out.println("Here");
			
		}
		
	}
}
