package repair.regen.processor.context;

import java.util.List;

import repair.regen.language.function.FunctionDeclaration;
import repair.regen.processor.constraints.Constraint;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

public class GhostState extends GhostFunction{
	
	private GhostFunction parent;
	private Constraint refinement;

	public GhostState(String name, List<String> param_types, CtTypeReference<?> return_type, Factory factory,
			String path, String klass, int group, int order) {
		super(name, param_types, return_type, factory, path, klass, group, order);
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
