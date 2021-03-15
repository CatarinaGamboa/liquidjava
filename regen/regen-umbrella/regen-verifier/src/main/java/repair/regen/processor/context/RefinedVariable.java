package repair.regen.processor.context;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;

import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.Predicate;
import spoon.reflect.reference.CtTypeReference;

public abstract class RefinedVariable extends Refined{
	private List<CtTypeReference<?>> supertypes;

	public RefinedVariable(String name, CtTypeReference<?> type, Constraint c) {
		super(name, type, c);
		supertypes = new ArrayList<>();
	}
	
	public abstract Constraint getMainRefinement();
	
	
	public void addSuperType(CtTypeReference<?> t) {
		if(!supertypes.contains(t))
			supertypes.add(t);
	}
	
	public List<CtTypeReference<?>> getSuperTypes(){
		return supertypes;
	}

	public void addSuperTypes(CtTypeReference<?> ts, Set<CtTypeReference<?>> sts) {
		if(ts != null && !supertypes.contains(ts))
			supertypes.add(ts);
		for(CtTypeReference<?> ct: sts)
			if(ct!=null && !supertypes.contains(ct))
				supertypes.add(ct);
		
	}

}
