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
import repair.regen.language.alias.Alias;
import repair.regen.language.alias.AliasUsage;
import repair.regen.language.function.Argument;
import repair.regen.language.function.FollowUpArgument;
import repair.regen.language.function.FunctionInvocationExpression;
import repair.regen.language.function.ObjectFieldInvocation;
import repair.regen.language.operators.NotOperator;
import repair.regen.language.parser.RefinementParser;
import repair.regen.language.parser.SyntaxException;
import repair.regen.processor.context.Context;
import repair.regen.processor.context.GhostFunction;

public class Predicate extends Constraint{
	private Expression exp;
	
	/**
	 * Create a predicate with the expression true
	 */
	public Predicate() {
		exp = new BooleanLiteral(true);
	}

	public Predicate(String ref) {
//		long a = System.currentTimeMillis();
//		System.out.println("Beginning new predicate");
		exp = parse(ref);
		if(!(exp instanceof ExpressionGroup) && !(exp instanceof LiteralExpression)) {
			exp = new ExpressionGroup(exp);
		}
//		long b = System.currentTimeMillis();
//		System.out.println("End new predicate:" + (b-a));
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
		Predicate c = new Predicate();
		c.setExpression(new UnaryExpression(new NotOperator(), exp));
		return c;
	}

	@Override
	public Constraint substituteVariable(String from, String to) {
		Predicate c = (Predicate) this.clone();
		if(from.equals(to))
			return c;
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
		return exp instanceof BooleanLiteral; 
//				toString().equals("true") || toString().equals("(true)") ;	
	}
	
	@Override
	public String toString() {
		return exp.toString();
	}

	@Override
	public Constraint clone() {
//		System.out.println("Beginning clone");
//		long a = System.currentTimeMillis();
		Constraint c = new Predicate(exp.toString());
//		long b = System.currentTimeMillis();
//		System.out.println("End new clone:" + (b-a));
		return c;
	}


	private void printSyntaxError(String ref) {
		System.out.println("______________________________________________________");
		System.err.println("Syntax error");
		System.out.println("Found in refinement:");
		System.out.println(ref);
		System.out.println("______________________________________________________");
		System.exit(2);
		
	}

	public List<GhostFunction> getGhostInvocations(List<GhostFunction> contextGhosts) {
		List<GhostFunction> gh = new ArrayList<>();
		auxGetGhostInvocations(exp, gh, contextGhosts);
		return gh;
	}

	private void auxGetGhostInvocations(Expression exp, List<GhostFunction> gh, List<GhostFunction> contextGhosts) {
		if(exp instanceof UnaryExpression)
			auxGetGhostInvocations(((UnaryExpression) exp).getExpression(), gh, contextGhosts);
		else if(exp instanceof BinaryExpression) {
			auxGetGhostInvocations(((BinaryExpression) exp).getFirstExpression(), gh, contextGhosts);
			auxGetGhostInvocations(((BinaryExpression) exp).getSecondExpression(), gh, contextGhosts);
		}else if(exp instanceof ExpressionGroup) {
			auxGetGhostInvocations(((ExpressionGroup) exp).getExpression(), gh, contextGhosts);	
		}else if(exp instanceof IfElseExpression) {
			auxGetGhostInvocations(((IfElseExpression) exp).getCondition(), gh, contextGhosts);
			auxGetGhostInvocations(((IfElseExpression) exp).getThenExpression(), gh, contextGhosts);
			auxGetGhostInvocations(((IfElseExpression) exp).getElseExpression(), gh, contextGhosts);
		}else if(exp instanceof AliasUsage) {
			for(Expression e :((AliasUsage)exp).getExpressions())
				auxGetGhostInvocations(e, gh, contextGhosts);
		}else if(exp instanceof FunctionInvocationExpression) {
			FunctionInvocationExpression fie = (FunctionInvocationExpression)exp;
			for(GhostFunction f: contextGhosts) {
				if(f.getName().equals(fie.getFunctionName()))
					gh.add(f);
			}
		}
		
	}

}
