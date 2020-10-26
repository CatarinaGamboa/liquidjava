package repair.regen.processor;

import spoon.reflect.reference.CtTypeReference;

public class VariableInfo {
	private String name;
	private CtTypeReference<?> type;
	private String refinements;
	
	public VariableInfo(String name, CtTypeReference<?> type, String refinements) {
		this.name = name;
		this.type = type;
		this.refinements = refinements;
	}
	
	public String getRefinement() {
		return refinements;
	}
	public CtTypeReference<?> getType(){
		return type;
	}
	
	public String getRenamedRefinements(String newName) {
		return refinements.replaceAll(name, newName);
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "VariableInfo [name=" + name + ", type=" + type + ", refinements=" + refinements + "]";
	}

}
