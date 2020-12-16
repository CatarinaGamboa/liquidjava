package repair.regen.processor;

import java.util.ArrayList;
import java.util.List;

import spoon.reflect.reference.CtTypeReference;

public class VariableInfo {
	private String name;
	private CtTypeReference<?> type;
	private List<String> refinements;
	private String incognitoName;
	
	public VariableInfo(String name, CtTypeReference<?> type, String refinement) {
		this.name = name;
		this.type = type;
		this.refinements = new ArrayList<String>();
		this.refinements.add(refinement);
	}
	
	/**
	 * Gets last refinement
	 * @return
	 */
	public String getRefinement() {
		if(refinements.size() > 0)
			return refinements.get(refinements.size()-1);
		return "";
		//return String.join(" && ", refinements);
	}
	public CtTypeReference<?> getType(){
		return type;
	}
	
	public String getRenamedRefinements() {
		return getRefinement().replaceAll(name, incognitoName);
	}
	
	public String getRenamedRefinements(String toReplace) {
		return getRefinement().replaceAll(name, toReplace);
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
	
	public void newRefinement(String toAdd) {
		refinements.add(toAdd);
	}
	public void removeRefinement(String toRemove) {
		refinements.remove(toRemove);
	}
	
	@Override
	public String toString() {
		return "VariableInfo [name=" + name + ", type=" + type + ", refinements=" + refinements + "]";
	}

}
