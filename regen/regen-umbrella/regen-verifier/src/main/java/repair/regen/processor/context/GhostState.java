package repair.regen.processor.context;

import java.util.List;

import repair.regen.language.function.FunctionDeclaration;
import repair.regen.processor.constraints.Constraint;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

public class GhostState extends GhostFunction{
	
	private GhostFunction parent;
	private Constraint refinement;

	public GhostState(String name, List<CtTypeReference<?>> list, CtTypeReference<?> return_type, String klass) {
		super(name, list, return_type, klass);
		// TODO Auto-generated constructor stub
	}
	
	public void setGhostParent(GhostFunction parent) {
		this.parent = parent;
	}
	public void setRefinement(Constraint c) {
		refinement = c;
	}
	
	public GhostFunction getParent() {
		return parent;
	}
	
	public Constraint getRefinement() {
		return refinement;
	}



}
