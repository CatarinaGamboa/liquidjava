package repair.regen.processor.constraints;

import java.util.List;

public abstract class Constraint {
	
	public abstract void substituteVariable(String from, String to);
	public abstract void negate();
	public abstract Constraint clone();
	public abstract List<String> getVariableNames();
	

}