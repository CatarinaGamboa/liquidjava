package repair.regen.processor.context;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import repair.regen.processor.constraints.Conjunction;
import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.Predicate;
import spoon.reflect.reference.CtTypeReference;

public class RefinedFunction extends Refined{
	
	private List<RefinedVariable> argRefinements;
	
	private Context context;
	
	public RefinedFunction() {
		argRefinements= new ArrayList<>();
		context = Context.getInstance();
	}
	
	public List<RefinedVariable> getArgRefinements() {
		return argRefinements;
	}
	
	public void addArgRefinements(String varName, CtTypeReference<?> type, Constraint refinement) {
		RefinedVariable v = new RefinedVariable(varName, type, refinement);
		this.argRefinements.add(v);

	}
	
	public void addArgRefinements(RefinedVariable vi) {
		this.argRefinements.add(vi);
	}
	
	public Constraint getRefReturn() {
		return super.getRefinement();
	}
	
	public void setRefReturn(Constraint ref) {
		super.setRefinement(ref);
	}
	
	public Constraint getRenamedRefinements() {
		return getRenamedRefinements(getAllRefinements());
	}
	
	private Constraint getRenamedRefinements(Constraint place) {
		Constraint update = place.clone();
		for(RefinedVariable p: argRefinements) {
			String varName = p.getName();
			Constraint c = p.getRefinement();
			Optional<RefinedVariable> ovi = p.getLastInstance();
			if(ovi.isPresent()) {
				varName = ovi.get().getName();
				c = p.getRenamedRefinements(varName);
			}
			context.addVarToContext(varName, p.getType(), c);
			update = update.substituteVariable(p.getName(), varName);
		}
		return update;
	}
	
	public Constraint getRenamedReturn() {
		return getRenamedRefinements(super.getRefinement());
	}

	public Constraint getAllRefinements() {
		Constraint c = new Predicate();
		for(RefinedVariable p: argRefinements)
			c = new Conjunction(c, p.getRefinement());//joinArgs
		c = new Conjunction(c, super.getRefinement());//joinReturn
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
			c = new Conjunction(c, argRefinements.get(i).getRefinement());
		return c;
	}

	@Override
	public String toString() {
		return "Function [name=" + super.getName() + ", argRefinements=" +
					argRefinements + ", refReturn=" + super.getRefinement() + "]";
	}
	

}
