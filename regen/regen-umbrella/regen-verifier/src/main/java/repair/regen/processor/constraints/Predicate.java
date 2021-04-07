package repair.regen.processor.constraints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import repair.regen.language.Expression;
import repair.regen.processor.context.GhostState;
import repair.regen.rj_language.ParsingException;
import repair.regen.rj_language.RefinementsParser;
import repair.regen.utils.ErrorPrinter;
import spoon.reflect.declaration.CtElement;

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


	protected Expression parse(String ref, CtElement element) {
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
			ErrorPrinter.printSyntaxError(e1.getMessage(), ref, element);
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
			String ns = RefinementsParser.substitute(s, from, to);
			return new Predicate(ns);
		}catch(ParsingException e) {
			ErrorPrinter.printSyntaxError(e.getMessage(), s);
		}
		
//		Predicate c = (Predicate) this.clone();
//		if(from.equals(to))
//			return c;
//		AuxVisitTree.substituteVariable(c.getExpression(), from, to);
//		c.exp.substituteVariable(from, to);
		return null;
	}

	@Override
	public List<String> getVariableNames() {
		List<String> l = new ArrayList<>();
		try {
			l = RefinementsParser.getVariableNames(s);
			return l;
		}catch(ParsingException e) {
			ErrorPrinter.printSyntaxError(e.getMessage(), s);
		}
		return l;
	}
	
	public List<GhostState> getGhostInvocations(List<GhostState> lgs) {
		List<String> ls = lgs.stream().map(p->p.getName()).collect(Collectors.toList());
		List<String> l = null;
		try {
			l = RefinementsParser.getGhostInvocations(s, ls);
		} catch (ParsingException e) {
			ErrorPrinter.printSyntaxError(e.getMessage(), s);
		}
		
		List<GhostState> gh = new ArrayList<>();
		for(String n: l) {
			for(GhostState g:lgs)
				if(g.getName().equals(n))
					gh.add(g);
		}

		return gh;
	}

	public Constraint changeOldMentions(String previousName, String newName) {
		try {
			String ns = RefinementsParser.changeOldTo(s, previousName, newName);
			return new Predicate(ns);
		} catch (ParsingException e) {
			ErrorPrinter.printSyntaxError(e.getMessage(), s);
		}
		return null;
	}
	
	@Override
	public Constraint changeStatesToRefinements(List<GhostState> ghostState, String[] toChange) {
		Map<String,String> nameRefinementMap = new HashMap<>();
		for(GhostState gs: ghostState)
			nameRefinementMap.put(gs.getName(), gs.getRefinement().toString());
		
		try {
			String ns = RefinementsParser.changeStateRefinement(s, nameRefinementMap, toChange);
			return new Predicate(ns);
		} catch (Exception e) {
			ErrorPrinter.printSyntaxError(e.getMessage(), s);
		}
		return null;
	}


	public boolean isBooleanTrue() {//TODO rever
		try {
			return RefinementsParser.isTrue(s);
		} catch (ParsingException e) {
			ErrorPrinter.printSyntaxError(e.getMessage(), s);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return s;
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
