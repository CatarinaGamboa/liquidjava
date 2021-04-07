package repair.regen.processor.constraints;

import java.util.List;

import repair.regen.language.Expression;
import repair.regen.processor.context.GhostState;
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
	

}
