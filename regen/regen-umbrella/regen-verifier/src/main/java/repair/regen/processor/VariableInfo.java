package repair.regen.processor;

import spoon.reflect.reference.CtTypeReference;

public class VariableInfo {
	private String name;
	private CtTypeReference<?> type;
	private String refinements;
	private String incognitoName;
	
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
	
	public String getRenamedRefinements() {
		return refinements.replaceAll(name, incognitoName);
	}

	public String getName() {
		return name;
	}
	
	public String getIncognitoName() {
		return hasIncognitoName()? incognitoName: null;
	}
	
	public void setIncognitoName(String name) {
		incognitoName = name;
	}
	
	public boolean hasIncognitoName() {
		return incognitoName != null;
	}
	
	@Override
	public String toString() {
		return "VariableInfo [name=" + name + ", type=" + type + ", refinements=" + refinements + "]";
	}

}
