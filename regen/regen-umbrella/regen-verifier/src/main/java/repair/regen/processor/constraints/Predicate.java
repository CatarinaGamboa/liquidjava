package repair.regen.processor.constraints;

import java.util.Optional;

import org.modelcc.parser.ParserException;

import repair.regen.language.BinaryExpression;
import repair.regen.language.Expression;
import repair.regen.language.ExpressionGroup;
import repair.regen.language.UnaryExpression;
import repair.regen.language.Variable;
import repair.regen.language.parser.RefinementParser;
import repair.regen.language.parser.SyntaxException;

public class Predicate extends Constraint{
	private Expression exp;

	public Predicate(String ref) {
		try{
			Optional<Expression> oe = RefinementParser.parse(ref);
			if(oe.isPresent()) {
				Expression e = oe.get();
				exp = e;
			}
		} catch (SyntaxException e1) {
			e1.printStackTrace();
		}
	}
		
	@Override
	public void substituteVariable(String from, String to) {
		auxSubstitute(exp, from, to);
	}
	
	private void auxSubstitute(Expression exp2, String from, String to) {
		if(exp2 instanceof Variable && 
				((Variable) exp2).getName().equals(from)){
			((Variable) exp2).changeName(to);
		}else if(exp2 instanceof BinaryExpression) {
			BinaryExpression be = (BinaryExpression) exp2;
			auxSubstitute(be.getFirstExpression(), from, to);
			auxSubstitute(be.getSecondExpression(), from, to);
		}else if(exp2 instanceof UnaryExpression) {
			UnaryExpression ue = (UnaryExpression)exp2;
			auxSubstitute(ue.getExpression(), from, to);
		}else if(exp2 instanceof ExpressionGroup) {
			auxSubstitute(((ExpressionGroup)exp2).getExpression(), from, to);
		}
		
	}

	public String toString() {
		return exp.toString();
	}
}
