package repair.regen.processor.constraints;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import repair.regen.ast.Expression;
import repair.regen.ast.UnaryExpression;
import repair.regen.processor.context.AliasWrapper;
import repair.regen.processor.context.Context;
import repair.regen.processor.context.GhostState;
import repair.regen.rj_language.ParsingException;
import repair.regen.rj_language.RefinementsParser;
import repair.regen.utils.ErrorPrinter;
import repair.regen.utils.Pair;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.factory.Factory;

public abstract class Constraint {
	
	public abstract Constraint substituteVariable(String from, String to);
	
	public abstract Constraint clone();
	public abstract List<String> getVariableNames();
	public abstract String toString();
	public abstract boolean isBooleanTrue();
	public abstract Constraint changeOldMentions(String previousName, String newName);
	public abstract Constraint changeStatesToRefinements(List<GhostState> ghostState, String[] toChange);
	public abstract Expression getExpression();
	
	
	public Constraint negate() {
		Expression e = new UnaryExpression("!", getExpression());
		return new Predicate(e);
	}

	protected Expression parse(String ref) {
		try{
			return RefinementsParser.createAST(ref);	 
		} catch (ParsingException e1) {
			ErrorPrinter.printSyntaxError(e1.getMessage(), ref);
		}	
		return null;
	}

	
	public Constraint changeAliasToRefinement(Context context, CtElement element, Factory f) throws Exception {
		Expression ref = getExpression();
		
		HashMap<String, Pair<Expression, List<Expression>>> mapAlias = new HashMap();
		for(AliasWrapper aw : context.getAlias()) {
			List<Expression> le = aw.getVarNames().stream().map(p->parse(p)).collect(Collectors.toList());
			Pair<Expression, List<Expression>> p = 
					new Pair<>(aw.getClonedConstraint().getExpression(), le);
			mapAlias.put(aw.getName(), p);
		}
		ref = ref.changeAlias(mapAlias, context, f);
		return new Predicate(ref);
	}

	

}
