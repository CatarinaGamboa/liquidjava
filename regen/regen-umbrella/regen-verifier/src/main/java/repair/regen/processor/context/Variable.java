package repair.regen.processor.context;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

import repair.regen.processor.constraints.Conjunction;
import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.EqualsPredicate;
import repair.regen.processor.constraints.IfThenElse;
import repair.regen.processor.constraints.Predicate;
import spoon.reflect.reference.CtTypeReference;

public class Variable extends RefinedVariable{
	//Specific Values
	private Stack<List<VariableInstance>> instances;
	
	//To combine if values
	private static final int ifbeforeIndex = 0;
	private static final int ifthenIndex = 1;
	private static final int ifelseIndex = 2;
	
	private Stack<Object[]> ifCombiner;//Optional<VariableInstance>

	
	public Variable(String name, CtTypeReference<?> type, Constraint ref) {
		super(name, type, ref);
		this.instances = new Stack<>();
		this.instances.push(new ArrayList<VariableInstance>());
		ifCombiner = new Stack<>();
	}

	
	
	public Constraint getRefinement() {
		Constraint c = super.getRefinement();
		Optional<VariableInstance> ovi =getLastInstance();
		if(ovi.isPresent()) {
			VariableInstance vi = ovi.get();
			c = Conjunction.createConjunction(new EqualsPredicate(this.getName(), vi.getName()), c);
		}
		return c;
	}
	
	public Constraint getMainRefinement() {
		return super.getRefinement();
	}
	

	
	//INSTANCES
	public void enterContext() {
		instances.push(new ArrayList<>());
	}
	
	public void exitContext() {
		instances.pop();
		
	}

	public void addInstance(VariableInstance vi) {
		instances.peek().add(vi);
	}
	
	public void removeLastInstance() {
		if(instances.size() > 0) 
			instances.peek().remove(instances.size()-1);
	}
	
	public Optional<VariableInstance> getLastInstance() {
		Stack<List<VariableInstance>> backup = new Stack<>();
		while(instances.size() > 0) {
			List<VariableInstance> lvi = instances.peek();
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
	
	private void reloadFromBackup(Stack<List<VariableInstance>> backup) {
		while(backup.size() > 0)
			instances.add(backup.pop());
	}
	
	
	//IFS
	public void newIfCombination() {
		ifCombiner.push(new Object[ifelseIndex+1]);
	}
	public void finishIfCombination() {
		ifCombiner.pop();
	}
	void saveInstanceBeforeIf() {
		if(!ifCombiner.isEmpty() && getLastInstance().isPresent())
			ifCombiner.peek()[ifbeforeIndex] = getLastInstance().get();
	}
	void saveInstanceThen() {
		if(!ifCombiner.isEmpty() && getLastInstance().isPresent())
			ifCombiner.peek()[ifthenIndex] = getLastInstance().get();
	}
	void saveInstanceElse() {
		if(!ifCombiner.isEmpty() && getLastInstance().isPresent())
			ifCombiner.peek()[ifelseIndex] = getLastInstance().get();
	}
	
	
	/**
	 * Creates a combination of the values introduced during the if then else
	 * The values depend on the calls to saveInstanceBeforeIf,
	 * saveInstanceThen and saveInstanceElse
	 * @param counter A number to create a new variable name
	 * @param cond 
	 * @return A new VariableInfo created by the combination of 
	 * 		   refinements or an empty Optional
	 */
	Optional<VariableInstance> getIfInstanceCombination(int counter, Constraint cond) {
		if(ifCombiner.isEmpty() ||
		  (!has(ifthenIndex) && !has(ifelseIndex) && !has(ifbeforeIndex)))
			return Optional.empty();
		
		String nName = String.format("#%s_%d",super.getName(),counter);
		Constraint ref = new Predicate();
		
		if(!has(ifelseIndex)) {
			if(has(ifbeforeIndex) && has(ifthenIndex)) 	//value before if and inside then
				ref = createITEConstraint(nName, cond, get(ifthenIndex), get(ifbeforeIndex));
			else if(!has(ifbeforeIndex))				//only value inside then
				ref = createITEConstraint(nName, cond, get(ifthenIndex));
		}else {
			if(has(ifthenIndex))						//value in then and in else
				ref = createITEConstraint(nName, cond, get(ifthenIndex), get(ifelseIndex));
			else if(has(ifbeforeIndex))				//value before and in else
				ref = createITEConstraint(nName, cond, get(ifbeforeIndex), get(ifelseIndex));
			else
				ref = createITEConstraint(nName, cond.negate(), get(ifelseIndex));	
		}
		return Optional.of(new VariableInstance(nName, super.getType(), ref));
	}


	private boolean has(int index) {
		Object[] l = ifCombiner.peek();
		Object o = ifCombiner.peek()[index];
		boolean b = o != null && (o instanceof VariableInstance);
		return b;
	}
	
	private VariableInstance get(int index) {
		return (VariableInstance) ifCombiner.peek()[index];
	}

	/**
	 * Creates an ITE where the else is true
	 * @param nName 
	 * @param cond
	 * @param ifThen
	 * @return
	 */
	private Constraint createITEConstraint(String nName, Constraint cond, VariableInstance then) {
		Constraint ref1 = then.getRenamedRefinements(nName);
		return new IfThenElse(cond, ref1, new Predicate());
	}

	private Constraint createITEConstraint(String nName, Constraint cond, VariableInstance then, VariableInstance els) {
		Constraint ref1 = then.getRenamedRefinements(nName);
		Constraint ref2 = els.getRenamedRefinements(nName);
		return new IfThenElse(cond, ref1, ref2);
	}

	@Override
	public String toString() {
		return "VariableInfo [name=" + super.getName() + ", type=" + super.getType() + ", refinement=" +
				super.getRefinement() +"]";
	}




}
