package repair.regen.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import spoon.reflect.reference.CtTypeReference;

public class VariableInfo {
	private String name;
	private CtTypeReference<?> type;
	private String mainRefinement;
	private List<String> refinements;
	private String incognitoName;
	
	private List<VariableInfo> instances;
	
	public VariableInfo(String name, CtTypeReference<?> type, String refinement) {
		this.name = name;
		this.type = type;
		this.refinements = new ArrayList<String>();
		this.refinements.add(refinement);
		this.instances = new ArrayList<>();
	}
	
	public void setMainRefinement(String main) {
		mainRefinement = main;
	}
	public String getMainRefinement() {
		return mainRefinement == null?"true":mainRefinement;
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
	
	public void addInstance(VariableInfo vi) {
		instances.add(vi);
	}
	public void removeLastInstance() {
		if(instances.size() > 0)
			instances.remove(instances.size()-1);
	}
	public Optional<VariableInfo> getLastInstance() {
		if(instances.size() > 0)
			return Optional.of(instances.get(instances.size()-1));
		return Optional.empty();
	}
	
	@Override
	public String toString() {
		return "VariableInfo [name=" + name + ", type=" + type + ", refinements=" +
				refinements + ", mainRefinement=" + mainRefinement+ 
				", instances=" + instances + "]";
	}

}
