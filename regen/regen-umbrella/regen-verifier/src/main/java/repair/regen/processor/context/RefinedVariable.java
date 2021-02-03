package repair.regen.processor.context;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.Predicate;
import spoon.reflect.reference.CtTypeReference;

public class RefinedVariable {
	private String name;
	private CtTypeReference<?> type;
	private Constraint refinement;

	//Specific Values
	private Stack<List<RefinedVariable>> instances;
	
	//To combine if values
	private RefinedVariable ifBefore;
	private RefinedVariable ifThen;
	private RefinedVariable ifElse;
	
	public RefinedVariable(String name, CtTypeReference<?> type, Constraint ref) {
		this.name = name;
		this.type = type;
		this.refinement = ref;
		this.instances = new Stack<>();
		this.instances.push(new ArrayList<RefinedVariable>());
	}

	/**
	 * Gets last refinement
	 * @return
	 */
	public Constraint getRefinement() {
		if(refinement != null)
			return refinement;
		return new Predicate("true");
		//return String.join(" && ", refinements);
	}
	public CtTypeReference<?> getType(){
		return type;
	}
	
	/**
	 * New Constraint with the variable name renamed to toReplace
	 * @param toReplace
	 * @return
	 */
	public Constraint getRenamedRefinements(String toReplace) {
		if(refinement instanceof Predicate) {
			Predicate np = new Predicate(refinement.toString());
			return np.substituteVariable(name, toReplace);
		}
		return refinement;//TODO REMOVE when all cases of Constraint are implemented
	}

	public String getName() {
		return name;
	}

	public void newRefinement(Constraint toAdd) {
		refinement=toAdd;
	}

	
	
	//INSTANCES
	public void enterContext() {
		instances.push(new ArrayList<>());
	}
	public void exitContext() {
		instances.pop();
	}
	
	public void addInstance(RefinedVariable vi) {
		System.out.println("add instance in variableInfo");
		instances.peek().add(vi);
	}
	
	public void removeLastInstance() {
		if(instances.size() > 0) 
			instances.peek().remove(instances.size()-1);
	}
	
	public Optional<RefinedVariable> getLastInstance() {
		Stack<List<RefinedVariable>> backup = new Stack<>();
		while(instances.size() > 0) {
			List<RefinedVariable> lvi = instances.peek();
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
	
	private void reloadFromBackup(Stack<List<RefinedVariable>> backup) {
		while(backup.size() > 0) {
			instances.add(backup.pop());
		}
	}
	
	//IFS
	void saveInstanceBeforeIf() {
		Optional<RefinedVariable> b = getLastInstance();
		if(b.isPresent())
			ifBefore = b.get();
	}
	void saveInstanceThen() {
		Optional<RefinedVariable> b = getLastInstance();
		if(b.isPresent())
			ifThen = b.get();
	}
	void saveInstanceElse() {
		Optional<RefinedVariable> b = getLastInstance();
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
	Optional<RefinedVariable> getIfInstanceCombination(int counter) {
		if(ifBefore == null && ifThen==null && ifElse==null)
			return Optional.empty();
		//TODO CHANGE ALMOST ALL
		String nName = name+"_"+counter+"_";
		String refinement = "";
		//no else 
		if(ifElse == null) {
			if(ifBefore != null && ifThen != null){
				Constraint ref1 = ifBefore.getRenamedRefinements(nName);
				Constraint ref2 = ifThen.getRenamedRefinements(nName);
				refinement = String.format("(%s)||(%s)", ref1.toString(), ref2.toString());//TODO CHANGE
			}else if(ifBefore == null) {//no value before if but has inside
				refinement = ifThen.getRenamedRefinements(nName).toString();
			}
		}else {//has else
			if(ifBefore != null && ifThen == null) {//no value in if
				String ref1 = ifBefore.getRenamedRefinements(nName).toString();
				String ref2 = ifElse.getRenamedRefinements(nName).toString();
				refinement = String.format("(%s)||(%s)", ref1, ref2);
			}else if(ifThen != null) {
				String ref1 = ifThen.getRenamedRefinements(nName).toString();
				String ref2 = ifElse.getRenamedRefinements(nName).toString();
				refinement = String.format("(%s)||(%s)", ref1, ref2);
			}else {//no value before and no value on if
				refinement = ifElse.getRenamedRefinements(nName).toString();
			}
		}
		ifBefore=null;ifThen=null;ifElse=null;
		return Optional.of(new RefinedVariable(nName, type, new Predicate(refinement)));//TODO CHANGE
	}

	@Override
	public String toString() {
		return "VariableInfo [name=" + name + ", type=" + type + ", refinement=" +
				refinement +
				", instances=" + instances + "]";
	}

}
