package repair.regen.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

import spoon.reflect.reference.CtTypeReference;

public class VariableInfo {
	private String name;
	private CtTypeReference<?> type;
	private String mainRefinement;
	private List<String> refinements;
	private String incognitoName;
	
	private Stack<List<VariableInfo>> instances;
	private VariableInfo ifBefore;
	private VariableInfo ifThen;
	private VariableInfo ifElse;
	
	public VariableInfo(String name, CtTypeReference<?> type, String refinement) {
		this.name = name;
		this.type = type;
		this.refinements = new ArrayList<String>();
		this.refinements.add(refinement);
		this.instances = new Stack<>();
		this.instances.push(new ArrayList<VariableInfo>());
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
	
	
	//INSTANCES
	public void enterContext() {
		instances.push(new ArrayList<>());
	}
	public void exitContext() {
		instances.pop();
	}
	
	public void addInstance(VariableInfo vi) {
		System.out.println("add instance in variableInfo");
		instances.peek().add(vi);
	}
	
	public void removeLastInstance() {
		if(instances.size() > 0) 
			instances.peek().remove(instances.size()-1);
	}
	
	public Optional<VariableInfo> getLastInstance() {
		Stack<List<VariableInfo>> backup = new Stack<>();
		while(instances.size() > 0) {
			List<VariableInfo> lvi = instances.peek();
			if(lvi.size() > 0) {//last list in stack has a value
				reloadFromBackup(backup);
				return Optional.of(lvi.get(lvi.size()-1));
			}else {
				backup.add(instances.pop());
			}
		}
		reloadFromBackup(backup);
		return Optional.empty();
	}
	
	private void reloadFromBackup(Stack<List<VariableInfo>> backup) {
		while(backup.size() > 0) {
			instances.add(backup.pop());
		}
	}
	
	//IFS
	void saveInstanceBeforeIf() {
		Optional<VariableInfo> b = getLastInstance();
		if(b.isPresent())
			ifBefore = b.get();
	}
	void saveInstanceThen() {
		Optional<VariableInfo> b = getLastInstance();
		if(b.isPresent())
			ifThen = b.get();
	}
	void saveInstanceElse() {
		Optional<VariableInfo> b = getLastInstance();
		if(b.isPresent())
			ifElse = b.get();
	}
	
	/**
	 * Creates a combination of the values introduced during the if then else
	 * The values depend on the calls to saveInstanceBeforeIf,
	 * saveInstanceThen and saveInstanceElse
	 * @param counter A number to create a new variable name
	 * @return A new VariableInfo created by the combination of 
	 * 		   refinements or an empty Optional
	 */
	Optional<VariableInfo> getIfInstanceCombination(int counter) {
		if(ifBefore == null && ifThen==null && ifElse==null)
			return Optional.empty();
		String nName = name+"_"+counter+"_";
		String refinement = "";
		//no else 
		if(ifElse == null) {
			if(ifBefore != null && ifThen != null){
				String ref1 = ifBefore.getRenamedRefinements(nName);
				String ref2 = ifThen.getRenamedRefinements(nName);
				refinement = String.format("(%s)||(%s)", ref1, ref2);
			}else if(ifBefore == null) {//no value before if but has inside
				refinement = ifThen.getRenamedRefinements(nName);
			}
		}else {//has else
			if(ifBefore != null && ifThen == null) {//no value in if
				String ref1 = ifBefore.getRenamedRefinements(nName);
				String ref2 = ifElse.getRenamedRefinements(nName);
				refinement = String.format("(%s)||(%s)", ref1, ref2);
			}else if(ifThen != null) {
				String ref1 = ifThen.getRenamedRefinements(nName);
				String ref2 = ifElse.getRenamedRefinements(nName);
				refinement = String.format("(%s)||(%s)", ref1, ref2);
			}else {//no value before and no value on if
				refinement = ifElse.getRenamedRefinements(nName);
			}
		}
		ifBefore=null;ifThen=null;ifElse=null;
		return Optional.of(new VariableInfo(nName, type, refinement));
	}

	@Override
	public String toString() {
		return "VariableInfo [name=" + name + ", type=" + type + ", refinements=" +
				refinements + ", mainRefinement=" + mainRefinement+ 
				", instances=" + instances + "]";
	}

}
