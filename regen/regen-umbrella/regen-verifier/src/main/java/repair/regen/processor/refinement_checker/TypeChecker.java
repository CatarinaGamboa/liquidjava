package repair.regen.processor.refinement_checker;

import java.lang.annotation.Annotation;
import java.util.Optional;

import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.Predicate;
import repair.regen.processor.context.Context;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.visitor.CtScanner;

public abstract class TypeChecker extends CtScanner{
	public final String REFINE_KEY = "refinement";
	public final String WILD_VAR = "_";
	public final String freshFormat = "#fresh_%d";
	public final String instanceFormat = "#%s_%d";
	String[] implementedTypes = {"boolean", "int", "short", "long", "float","double", "int[]"}; //TODO add types
	
	Context context;
	
	public TypeChecker(Context c) {
		this.context = c;
	}

	
	public Optional<Constraint> getRefinementFromAnnotation(CtElement element) {
		Optional<Constraint> constr = Optional.empty();
		Optional<String> ref = Optional.empty();
		for(CtAnnotation<? extends Annotation> ann :element.getAnnotations()) 
			if( ann.getActualAnnotation().annotationType().getCanonicalName()
					.contentEquals("repair.regen.specification.Refinement")) {
				CtLiteral<String> s = (CtLiteral<String>) ann.getAllValues().get("value");
				ref = Optional.of(s.getValue());
			}		
		if(ref.isPresent()) 
			constr = Optional.of(new Predicate(ref.get()));
		
		return constr;
	}

	Constraint getRefinement(CtElement elem) {
		return (Constraint)elem.getMetadata(REFINE_KEY);
	}
	
	abstract void checkVariableRefinements(Constraint refinementFound, String simpleName, CtVariable<?> variable);
	abstract void checkSMT(Constraint expectedType, CtElement element);

	
	
	
}
