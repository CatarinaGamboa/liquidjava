package repair.regen.processor.constraints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import repair.regen.ast.Expression;
import repair.regen.ast.GroupExpression;
import repair.regen.ast.LiteralBoolean;
import repair.regen.ast.UnaryExpression;
import repair.regen.processor.context.GhostState;
import repair.regen.rj_language.ParsingException;
import repair.regen.rj_language.RefinementsParser;
import repair.regen.utils.ErrorPrinter;

public class Predicate extends Constraint{
	private final String OLD = "old";
	protected Expression exp;
	/**
	 * Create a predicate with the expression true
	 */
	public Predicate() {
		exp = new LiteralBoolean(true);
	}

	public Predicate(String ref) {
		exp = parse(ref);
		if(!(exp instanceof GroupExpression)) {
			exp = new GroupExpression(exp);
		}
		parse(ref);
	}

	/**
	 * Create a predicate with the expression true
	 */
	public Predicate(Expression e) {
		exp = e;
	}

	@Override
	public Constraint negate() {
		return new Predicate(new UnaryExpression("!", exp));
	}

	@Override
	public Constraint substituteVariable(String from, String to) {
		Expression ec = exp.clone();
		ec.substitute(from, to);
		return new Predicate(ec);
	}

	@Override
	public List<String> getVariableNames() {
		List<String> l = new ArrayList<>();
		exp.getVariableNames(l);
		return l;
	}

	public List<GhostState> getStateInvocations(List<GhostState> lgs) {
		if(lgs == null)
			return new ArrayList<>();
		List<String> all = lgs.stream().map(p->p.getName()).collect(Collectors.toList());
		List<String> toAdd = new ArrayList<>();
		exp.getStateInvocations(toAdd, all);
		
		List<GhostState> gh = new ArrayList<>();
		for(String n: toAdd) {
			for(GhostState g:lgs)
				if(g.getName().equals(n))
					gh.add(g);
		}

		return gh;
	}

	public Constraint changeOldMentions(String previousName, String newName) {
		Expression e = exp.clone();
		Expression prev = parse(previousName);
		List<Expression> le = new ArrayList<>();
		le.add(parse(newName));
		e.substituteFunction(OLD, le, prev);
		return new Predicate(e);
	}

	@Override
	public Constraint changeStatesToRefinements(List<GhostState> ghostState, String[] toChange) {
		Map<String,Expression> nameRefinementMap = new HashMap<>();
		for(GhostState gs: ghostState)
			nameRefinementMap.put(gs.getName(), parse(gs.getRefinement().toString()));
		
		Expression e = exp.substituteState(nameRefinementMap, toChange);
		return new Predicate(e);
	}


	public boolean isBooleanTrue() {
		return exp.isBooleanTrue();
	}

	@Override
	public String toString() {
		return exp.toString();
	}

	@Override
	public Constraint clone() {
		Constraint c = new Predicate(exp.clone());
		return c;
	}

	@Override
	public Expression getExpression() {
		return exp;
	}

}
