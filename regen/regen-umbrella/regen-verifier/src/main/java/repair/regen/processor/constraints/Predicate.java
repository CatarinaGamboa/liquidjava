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
import repair.regen.processor.context.GhostState;
import repair.regen.rj_language.ParsingException;
import repair.regen.rj_language.RefinementsParser;

public class Predicate extends Constraint{
	
	private String s;
	/**
	 * Create a predicate with the expression true
	 */
	public Predicate() {
//		exp = new BooleanLiteral(true);
		s = "true";
	}

	public Predicate(String ref) {
//		exp = parse(ref);
//		if(!(exp instanceof ExpressionGroup) && !(exp instanceof LiteralExpression)) {
//			exp = new ExpressionGroup(exp);
//		}
//		
		s = ref;

		
		
	}
	
//	public Predicate(Expression exp) {
//		this.exp = exp;
//	}


	protected Expression parse(String ref) {
		try{
			//Optional<Expression> oe = RefinementParser.parse(ref);
			RefinementsParser.compile(ref);
			
			//System.out.println(oe.toString());
//			if(oe.isPresent()) {
//				Expression e = oe.get();
//				return e;
//			}
//			
//		} catch (SyntaxException e1) {
//			printSyntaxError(ref);
//			
		} catch (ParsingException e1) {
			// TODO Auto-generated catch block
			printSyntaxError(ref);
		}	
		return null;
	}
	
	
//	public Expression getExpression() {
//		return exp;
//	}
//	
//	protected void setExpression(Expression e) {
//		exp = e;
//	}
	
	
	@Override
	public Constraint negate() {
		return new Predicate(String.format("!(%s)", s));
	}

	@Override
	public Constraint substituteVariable(String from, String to) {
		try {
			String s = RefinementsParser.substitute(s, from, to);
		}catch(ParsingException e) {
			
		}
		
		Predicate c = (Predicate) this.clone();
		if(from.equals(to))
			return c;
		AuxVisitTree.substituteVariable(c.getExpression(), from, to);
//		c.exp.substituteVariable(from, to);
		return c;
	}

	@Override
	public List<String> getVariableNames() {
		List<String> l = new ArrayList<>();
		exp.getVariableNames(l);
		return l;
	}
	
	public List<GhostState> getGhostInvocations(List<GhostState> lgs) {
		List<GhostState> gh = new ArrayList<>();
		if(lgs != null)
			AuxVisitTree.getGhostInvocations(exp, gh, lgs);
		return gh;
	}

	public Constraint changeOldMentions(String previousName, String newName) {
		Constraint c = this.clone();
		AuxVisitTree.changeOldMentions(c.getExpression(), previousName, newName);
		return c;
	}
	
	@Override
	public Constraint changeStatesToRefinements(List<GhostState> ghostState) {
		Constraint c = this.clone();
		AuxVisitTree.changeStateRefinements(c.getExpression(), ghostState);
		return c;
	}


	public boolean isBooleanTrue() {//TODO rever
		return exp instanceof BooleanLiteral || AuxVisitTree.isTrue(exp);
	}
	
	@Override
	public String toString() {
		return exp.toString();
	}

	@Override
	public Constraint clone() {
//		System.out.println("Beginning clone");
//		long a = System.currentTimeMillis();
		Constraint c = new Predicate(s);
//		long b = System.currentTimeMillis();
//		System.out.println("End new clone:" + (b-a));
		return c;
	}





	

}
