package repair.regen.processor.context;

import repair.regen.processor.constraints.Constraint;
import spoon.reflect.reference.CtTypeReference;

public class VariableInstance extends RefinedVariable{
	
	private Constraint state;
	

	public VariableInstance(String name, CtTypeReference<?> type, Constraint c) {
		super(name, type, c);
	}
	
	@Override
	public Constraint getMainRefinement() {
		return super.getRefinement();
	}
	

	@Override
	public String toString() {
		return "RefinedInstance [name=" + super.getName() + 
				", type=" + super.getType() + ", refinement=" +
				super.getRefinement() +"]";
	}
	
	public void setState(Constraint c) {
		state = c;
	}
	public Constraint getState() {
		return state;
	}

}
