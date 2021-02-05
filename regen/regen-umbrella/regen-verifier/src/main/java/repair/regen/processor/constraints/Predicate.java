package repair.regen.processor.constraints;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import repair.regen.language.BinaryExpression;
import repair.regen.language.Expression;
import repair.regen.language.ExpressionGroup;
import repair.regen.language.UnaryExpression;
import repair.regen.language.Variable;
import repair.regen.language.parser.RefinementParser;
import repair.regen.language.parser.SyntaxException;

public class Predicate extends Constraint{
	private Expression exp;
	
	/**
	 * Create a predicate with the expression true
	 */
	public Predicate() {
		exp = parse("true");
	}

	public Predicate(String ref) {
		exp = parse(ref);
		if(!(exp instanceof ExpressionGroup)) {
			exp = parse(String.format("(%s)", ref));
		}
	}
	@Override
	public Constraint negate() {
		Predicate c = (Predicate)this.clone();
		c.exp = parse("(!("+exp.toString()+"))");
		return c;
	}

	@Override
	public Constraint substituteVariable(String from, String to) {
		Predicate c = (Predicate) this.clone();
		auxSubstitute(c.exp, from, to);
		return c;
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

	@Override
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
