package repair.regen.processor.context;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.Predicate;
import spoon.reflect.reference.CtTypeReference;

public abstract class RefinedVariable extends Refined{

	public RefinedVariable(String name, CtTypeReference<?> type, Constraint c) {
		super(name, type, c);
	}
	
	public abstract Constraint getMainRefinement();

}
