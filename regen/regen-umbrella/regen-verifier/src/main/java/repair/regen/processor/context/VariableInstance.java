package repair.regen.processor.context;

import java.util.Optional;

import repair.regen.processor.constraints.Constraint;
import spoon.reflect.reference.CtTypeReference;

public class VariableInstance extends RefinedVariable{
	
//	private Constraint state;
	private Variable parent;

	public VariableInstance(String name, CtTypeReference<?> type, Constraint c) {
		super(name, type, c);
		this.parent = null;
	}

	public VariableInstance(String name, CtTypeReference<?> type, Constraint c, Variable parent) {
		super(name, type, c);
		this.parent = parent;
	}
	
	@Override
	public Constraint getMainRefinement() {
		return super.getRefinement();
	}
	

	@Override
	public String toString() {
		return "VariableInstance [name=" + super.getName() + 
				", type=" + super.getType() + ", refinement=" +
				super.getRefinement() +"]";
	}

	public void setParent(Variable p) {
		parent = p;
	}
	
	public Optional<Variable> getParent() {
		return parent == null ? Optional.empty() : Optional.of(parent);
	}
	
//	public void setState(Constraint c) {
//		state = c;
//	}
//	public Constraint getState() {
//		return state;
//	}
//	
	
	

}
