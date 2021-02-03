package repair.regen.processor.constraints;

import java.util.ArrayList;
import java.util.List;
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
		exp = parse(ref);
	}

	@Override
	public void negate() {
		exp = parse("!("+exp.toString()+")");
	}

	@Override
	public void substituteVariable(String from, String to) {
		auxSubstitute(exp, from, to);
	}

	private Expression parse(String ref) {
		try{
			Optional<Expression> oe = RefinementParser.parse(ref);
			if(oe.isPresent()) {
				Expression e = oe.get();
				return e;
			}
		} catch (SyntaxException e1) {
			e1.printStackTrace();
		}	
		return null;
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

	@Override
	public Constraint clone() {
		return new Predicate(exp.toString());
	}

	@Override
	public List<String> getVariableNames() {
		List<String> l = new ArrayList<>();
		auxGetVariableNames(exp, l);
		return l;
	}

	private void auxGetVariableNames(Expression exp2, List<String> l) {
		if(exp2 instanceof Variable){
			l.add(((Variable) exp2).getName());
		}else if(exp2 instanceof BinaryExpression) {
			BinaryExpression be = (BinaryExpression) exp2;
			auxGetVariableNames(be.getFirstExpression(), l);
			auxGetVariableNames(be.getSecondExpression(), l);
		}else if(exp2 instanceof UnaryExpression) {
			UnaryExpression ue = (UnaryExpression)exp2;
			auxGetVariableNames(ue.getExpression(), l);
		}else if(exp2 instanceof ExpressionGroup) {
			auxGetVariableNames(((ExpressionGroup)exp2).getExpression(), l);
		}
	}

}