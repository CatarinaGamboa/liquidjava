package repair.regen.processor.constraints;

import java.util.List;

import repair.regen.language.Expression;

public abstract class Constraint {
	
	public abstract Constraint substituteVariable(String from, String to);
	public abstract Constraint negate();
	public abstract Constraint clone();
	public abstract List<String> getVariableNames();
	public abstract String toString();
	public abstract Expression getExpression();
	

}
