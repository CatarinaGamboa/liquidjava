package repair.regen.processor.constraints;

import java.util.List;

public abstract class Constraint {
	
	public abstract Constraint substituteVariable(String from, String to);
	public abstract Constraint negate();
	public abstract Constraint clone();
	public abstract List<String> getVariableNames();
	

}
