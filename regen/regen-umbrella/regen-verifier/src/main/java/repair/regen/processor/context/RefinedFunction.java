package repair.regen.processor.context;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
			//trocar varName por newName, adicionar newName ao context, trocar o varName por newName no update
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
		//TODO CHANGE to Conjunction
		StringBuilder sb = new StringBuilder();
		for(RefinedVariable p: argRefinements) {
			sb.append(p.getRefinement()+ " && ");
		}
		sb.append(super.getRefinement().toString());
		return new Predicate(sb.toString());
	}


	public Constraint getRefinementsForParamIndex(int i) {
		StringBuilder sb = new StringBuilder();
		for (int j = 0; j < i && j < argRefinements.size(); j++) {
			RefinedVariable vi = argRefinements.get(i);
			sb.append(vi.getRefinement()+ " && ");
		}
		sb.append(argRefinements.get(i).getRefinement());
		return new Predicate(sb.toString());//getRenamedRefinements();
	}

	@Override
	public String toString() {
		return "Function [name=" + super.getName() + ", argRefinements=" +
					argRefinements + ", refReturn=" + super.getRefinement() + "]";
	}
	

}
