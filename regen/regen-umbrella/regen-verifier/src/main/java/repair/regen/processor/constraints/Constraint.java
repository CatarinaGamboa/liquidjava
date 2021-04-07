package repair.regen.processor.constraints;

import java.util.HashMap;
import java.util.List;

import repair.regen.processor.context.AliasWrapper;
import repair.regen.processor.context.Context;
import repair.regen.processor.context.GhostState;
import repair.regen.rj_language.RefinementsParser;
import repair.regen.utils.ErrorPrinter;
import repair.regen.utils.Pair;
import spoon.reflect.declaration.CtElement;

public abstract class Constraint {
	
	public abstract Constraint substituteVariable(String from, String to);
	public abstract Constraint negate();
	public abstract Constraint clone();
	public abstract List<String> getVariableNames();
	public abstract String toString();
	public abstract boolean isBooleanTrue();
	public abstract Constraint changeOldMentions(String previousName, String newName);
	public abstract Constraint changeStatesToRefinements(List<GhostState> ghostState, String[] toChange);
	public abstract String getExpression();
	
	public Constraint changeAliasToRefinement(Context context, CtElement element) {
		String ref = getExpression();
		HashMap<String, Pair<String, List<String>>> m = new HashMap<>();
		for(AliasWrapper aw : context.getAlias()) {
			Pair<String, List<String>> p = new Pair(aw.getClonedConstraint().getExpression(), aw.getVarNames());
			m.put(aw.getName(), p);
		}
		String s;
		try {
			s = RefinementsParser.changeAlias(ref, m);
			return new Predicate(s);
		} catch (Exception e) {
			ErrorPrinter.printSyntaxError(e.getMessage(), ref, element);
		}
		return null;
	}
	

}
