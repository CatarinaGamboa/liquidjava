package repair.regen.processor.context;

import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.Predicate;
import spoon.reflect.reference.CtTypeReference;

public abstract class Refined {
	
	private String name;
	private CtTypeReference<?> type;
	private Constraint refinement;
	
	public Refined() {}
	
	public Refined(String name, CtTypeReference<?> type, Constraint refinement) {
		this.name = name;
		this.type = type;
		this.refinement = refinement;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public CtTypeReference<?> getType() {
		return type;
	}
	public void setType(CtTypeReference<?> type) {
		this.type = type;
	}
	
	public void setRefinement(Constraint c) {
		this.refinement = c;
	}
	
	public Constraint getRefinement() {
		if(refinement != null)
			return refinement;
		return new Predicate("true");
	}
	

	@Override
	public String toString() {
		return "Refined [name=" + name + ", type=" + type + ", refinement=" +
				refinement +"]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((refinement == null) ? 0 : refinement.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Refined other = (Refined) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (refinement == null) {
			if (other.refinement != null)
				return false;
		} else if (!refinement.equals(other.refinement))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

}
