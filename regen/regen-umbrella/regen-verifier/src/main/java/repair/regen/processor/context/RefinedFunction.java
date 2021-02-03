package repair.regen.processor.context;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.Predicate;
import spoon.reflect.reference.CtTypeReference;

public class RefinedFunction {
	
	private String name;
	private List<RefinedVariable> argRefinements;
	private CtTypeReference<?> type;
	private Constraint refReturn;

	
	private Context context;
	
	public RefinedFunction() {
		argRefinements= new ArrayList<>();
		context = Context.getInstance();
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
		return refReturn;
	}
	public void setRefReturn(Constraint ref) {
		this.refReturn = ref;
	}
	public CtTypeReference<?> getType() {
		return type;
	}
	public void setType(CtTypeReference<?> type) {
		this.type = type;
	}
	
	
	public Constraint getRenamedRefinements() {
		return getRenamedRefinements(getAllRefinements());
	}
	
//	private String getRenamedRefinements(String place) {
//		String update = place;
//		for(RefinedVariable p: argRefinements) {
//			String var = p.getName();
//			Optional<RefinedVariable> ovi = p.getLastInstance();
//			String newName = ovi.isPresent()? ovi.get().getName():var;
//			
//			p.getRenamedRefinements(newName);
//			
//			String newRefs = p.getRefinement().replaceAll(var, newName);
//			context.addVarToContext(newName, p.getType(), newRefs);
//			update = update.replaceAll(var, newName);
//		}
//		return update;
//	}
	
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
			update.substituteVariable(p.getName(), varName);
		}
		return update;
	}
	
	public Constraint getRenamedReturn() {
		return getRenamedRefinements(this.refReturn);
	}

	public Constraint getAllRefinements() {
		//TODO CHANGE to Conjunction
		StringBuilder sb = new StringBuilder();
		for(RefinedVariable p: argRefinements) {
			sb.append(p.getRefinement()+ " && ");
		}
		sb.append(refReturn);
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((argRefinements == null) ? 0 : argRefinements.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((refReturn == null) ? 0 : refReturn.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RefinedFunction other = (RefinedFunction) obj;
		if (argRefinements == null) {
			if (other.argRefinements != null)
				return false;
		} else if (!argRefinements.equals(other.argRefinements))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (refReturn == null) {
			if (other.refReturn != null)
				return false;
		} else if (!refReturn.equals(other.refReturn))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Function [name=" + name + ", argRefinements=" +
					argRefinements + ", refReturn=" + refReturn + "]";
	}
	

}
