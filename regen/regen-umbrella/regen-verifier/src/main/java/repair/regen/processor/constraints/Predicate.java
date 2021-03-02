package repair.regen.processor.constraints;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import repair.regen.language.BinaryExpression;
import repair.regen.language.BooleanLiteral;
import repair.regen.language.Expression;
import repair.regen.language.ExpressionGroup;
import repair.regen.language.IfElseExpression;
import repair.regen.language.LiteralExpression;
import repair.regen.language.UnaryExpression;
import repair.regen.language.Variable;
import repair.regen.language.function.Argument;
import repair.regen.language.function.FollowUpArgument;
import repair.regen.language.function.FunctionInvocationExpression;
import repair.regen.language.function.ObjectFieldInvocation;
import repair.regen.language.operators.NotOperator;
import repair.regen.language.parser.RefinementParser;
import repair.regen.language.parser.SyntaxException;

public class Predicate extends Constraint{
	private Expression exp;
	
	/**
	 * Create a predicate with the expression true
	 */
	public Predicate() {
		exp = new BooleanLiteral(true);
	}

	public Predicate(String ref) {
		exp = parse(ref);
		if(!(exp instanceof ExpressionGroup) && !(exp instanceof LiteralExpression)) {
			exp = parse(String.format("(%s)", ref));
		}
	}
	
	public Predicate(Expression exp) {
		this.exp = exp;
	}


	protected Expression parse(String ref) {
		try{
			Optional<Expression> oe = RefinementParser.parse(ref);
			//System.out.println(oe.toString());
			if(oe.isPresent()) {
				Expression e = oe.get();
				return e;
			}
			
		} catch (SyntaxException e1) {
			printSyntaxError(ref);
			
		}	
		return null;
	}
	
	
	public Expression getExpression() {
		return exp;
	}
	
	protected void setExpression(Expression e) {
		exp = e;
	}
	
	
	@Override
	public Constraint negate() {
		Predicate c = (Predicate)this.clone();
		c.exp = new UnaryExpression(new NotOperator(), exp);
		return c;
	}

	@Override
	public Constraint substituteVariable(String from, String to) {
		Predicate c = (Predicate) this.clone();
		c.exp.substituteVariable(from, to);
		return c;
	}

	@Override
	public List<String> getVariableNames() {
		List<String> l = new ArrayList<>();
		exp.getVariableNames(l);
		return l;
	}

	public boolean isBooleanTrue() {//TODO rever
		return toString().equals("true") || toString().equals("(true)") ;	
	}
	
	@Override
	public String toString() {
		return exp.toString();
	}

	@Override
	public Constraint clone() {
		return new Predicate(exp.toString());
	}


	private void printSyntaxError(String ref) {
		System.out.println("______________________________________________________");
		System.err.println("Syntax error");
		System.out.println("Found in refinement:");
		System.out.println(ref);
		System.out.println("______________________________________________________");
		System.exit(2);
		
	}

}
