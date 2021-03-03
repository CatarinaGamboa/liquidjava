package repair.regen.processor.context;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import repair.regen.processor.constraints.Conjunction;
import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.Predicate;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.reference.CtTypeReference;

public class RefinedFunction extends Refined{
	
	private List<Variable> argRefinements;	
	private String targetClass;
	private ObjectState stateChange;
	
	public RefinedFunction() {
		argRefinements= new ArrayList<>();
	}
	
	
	public List<Variable> getArguments() {
		return argRefinements;
	}
	
	public void addArgRefinements(String varName, CtTypeReference<?> type, Constraint refinement) {
		Variable v = new Variable(varName, type, refinement);
		this.argRefinements.add(v);

	}
	
	public void addArgRefinements(Variable vi) {
		this.argRefinements.add(vi);
	}
	
	public Constraint getRefReturn() {
		return super.getRefinement();
	}
	
	public void setRefReturn(Constraint ref) {
		super.setRefinement(ref);
	}
	
	
	public void setClass(String klass) {
		this.targetClass = klass;
	}
	
	public String getTargetClass() {
		return targetClass;
	}
	
	public Constraint getRenamedRefinements(Context c) {
		return getRenamedRefinements(getAllRefinements(), c);
	}
	
	private Constraint getRenamedRefinements(Constraint place, Context context) {
		Constraint update = place.clone();
		for(Variable p: argRefinements) {
			String varName = p.getName();
			Constraint c = p.getRefinement();
			Optional<VariableInstance> ovi = p.getLastInstance();
			if(ovi.isPresent()) {
				varName = ovi.get().getName();
				c = p.getRenamedRefinements(varName);
			}
			context.addVarToContext(varName, p.getType(), c);
			update = update.substituteVariable(p.getName(), varName);
		}
		return update;
	}


	public Constraint getAllRefinements() {
		Constraint c = new Predicate();
		for(RefinedVariable p: argRefinements)
			c = Conjunction.createConjunction(c, p.getRefinement());//joinArgs
		c = Conjunction.createConjunction(c, super.getRefinement());//joinReturn
		return c;
	}


	/**
	 * Gives the Constraint for a certain parameter index
	 * and regards all the previous parameters' Constraints
	 * @param index
	 * @return
	 */
	public Constraint getRefinementsForParamIndex(int index) {
		Constraint c = new Predicate();
		for (int i = 0; i <= index && i < argRefinements.size(); i++)
			c = Conjunction.createConjunction(c, argRefinements.get(i).getRefinement());
		return c;
	}
	
	public boolean allRefinementsTrue() {
		boolean t = true;
		Predicate p = new Predicate(getRefReturn().getExpression());
		t = t && p.isBooleanTrue();
		for(Variable v: argRefinements) {
			p = new Predicate(v.getRefinement().getExpression());
			t = t && p.isBooleanTrue();
		}
		return t;
	}
	
	
	public void setState(CtAnnotation<? extends Annotation> ctAnnotation) {
		Map<String, CtExpression> m = ctAnnotation.getAllValues();
		CtLiteral<String> from = (CtLiteral<String>)m.get("from");
		CtLiteral<String> to = (CtLiteral<String>)m.get("to");
		stateChange = new ObjectState();
		if(from != null)
			stateChange.setFrom(new Predicate(from.getValue()));
		if(to != null)
			stateChange.setTo(new Predicate(to.getValue()));
		System.out.println();
		
	}
	
	public Optional<Constraint> getStateFrom() {
		return stateChange.getFrom();
	}
	
	public Optional<Constraint> getStateTo() {
		return stateChange.getTo();
	}
	

	@Override
	public String toString() {
		return "Function [name=" + super.getName() + ", argRefinements=" +
					argRefinements + ", refReturn=" + super.getRefinement() + 
					", targetClass="+targetClass+"]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((argRefinements == null) ? 0 : argRefinements.hashCode());
		result = prime * result + ((targetClass == null) ? 0 : targetClass.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		RefinedFunction other = (RefinedFunction) obj;	
		if (argRefinements == null) {
			if (other.argRefinements != null)
				return false;
		} else if (argRefinements.size() != other.argRefinements.size())
			return false;
		if (targetClass == null) {
			if (other.targetClass != null)
				return false;
		} else if (!targetClass.equals(other.targetClass))
			return false;
		return true;
	}







}
