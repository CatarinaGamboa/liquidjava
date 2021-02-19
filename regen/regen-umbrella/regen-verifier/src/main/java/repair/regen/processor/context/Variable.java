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
	private List<VariableInstance> instances;
	
	//To combine if values
	private VariableInstance ifBefore;
	private VariableInstance ifThen;
	private VariableInstance ifElse;
	
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
	
	
	public Variable(String name, CtTypeReference<?> type, Constraint ref) {
		super(name, type, ref);
		this.instances = new ArrayList<VariableInstance>();
	}

	
	//INSTANCES
	public void enterContext() {
		//instances.push(new ArrayList<>());
	}
	public void exitContext() {
		//instances.pop();
	}
	
	public void addInstance(VariableInstance vi) {
		//instances.peek().add(vi);
		instances.add(vi);
	}
	
	public void removeLastInstance() {
		if(instances.size() > 0) 
			instances.remove(instances.size()-1);
	}
	
	public Optional<VariableInstance> getLastInstance() {
		if(instances.size()>0)
			return Optional.of(instances.get(instances.size()-1));
		return Optional.empty();
	}
	
	
	//IFS
	void saveInstanceBeforeIf() {
		Optional<VariableInstance> b = getLastInstance();
		if(b.isPresent())
			ifBefore = b.get();
	}
	void saveInstanceThen() {
		Optional<VariableInstance> b = getLastInstance();
		if(b.isPresent())
			ifThen = b.get();
	}
	void saveInstanceElse() {
		Optional<VariableInstance> b = getLastInstance();
		if(b.isPresent())
			ifElse = b.get();
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
		if(!has(ifThen) && !has(ifElse))
			return Optional.empty();
		
		String nName = String.format("#%s_%d",super.getName(),counter);
		Constraint ref = new Predicate();
		
		if(!has(ifElse)) {
			if(has(ifBefore) && has(ifThen)) 	//value before if and inside then
				ref = createITEConstraint(nName, cond, ifThen, ifBefore);
			else if(!has(ifBefore))				//only value inside then
				ref = createITEConstraint(nName, cond, ifThen);
		}else {
			if(has(ifThen))						//value in then and in else
				ref = createITEConstraint(nName, cond, ifThen, ifElse);
			else if(has(ifBefore))				//value before and in else
				ref = createITEConstraint(nName, cond, ifBefore, ifElse);
			else
				ref = createITEConstraint(nName, cond.negate(), ifElse);	
		}

		ifBefore=null;ifThen=null;ifElse=null;
		return Optional.of(new VariableInstance(nName, super.getType(), ref));
	}
	
	private boolean has(VariableInstance v) {
		return v!=null;
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
