package repair.regen.processor.refinement_checker;

import java.lang.annotation.Annotation;
import java.util.Optional;

import repair.regen.language.alias.Alias;
import repair.regen.language.function.FunctionDeclaration;
import repair.regen.language.parser.RefinementParser;
import repair.regen.language.parser.SyntaxException;
import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.Predicate;
import repair.regen.processor.context.AliasWrapper;
import repair.regen.processor.context.Context;
import repair.regen.processor.context.GhostFunction;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.CtScanner;

public abstract class TypeChecker extends CtScanner{
	public final String REFINE_KEY = "refinement";
	public final String STATE_KEY = "state";
	public final String THIS = "this";
	public final String WILD_VAR = "_";
	public final String freshFormat = "#fresh_%d";
	public final String instanceFormat = "#%s_%d";
	public final String thisFormat = "this#%s";
	String[] implementedTypes = {"boolean", "int", "short", "long", "float","double", "int[]"}; //TODO add types
	
	Context context;
	Factory factory;
	
	public TypeChecker(Context c, Factory fac) {
		this.context = c;
		this.factory = fac;
	}

	
	
	abstract void checkVariableRefinements(Constraint refinementFound, String simpleName, 
			CtTypeReference type, CtElement variable);
	abstract void checkSMT(Constraint expectedType, CtElement element);


	protected abstract void checkStateSMT(Constraint prevState, Constraint expectedState, CtExpression<?> target);

	
	
	
	public Optional<Constraint> getRefinementFromAnnotation(CtElement element) {
		Optional<Constraint> constr = Optional.empty();
		Optional<String> ref = Optional.empty();
		for(CtAnnotation<? extends Annotation> ann :element.getAnnotations()) { 
			String an = ann.getActualAnnotation().annotationType().getCanonicalName();
			if( an.contentEquals("repair.regen.specification.Refinement")) {
				CtLiteral<String> s = (CtLiteral<String>) ann.getAllValues().get("value");
				ref = Optional.of(s.getValue());
			}else if( an.contentEquals("repair.regen.specification.RefinementPredicate")) {
				CtLiteral<String> s = (CtLiteral<String>) ann.getAllValues().get("value");
				getGhostFunction(s.getValue(), element);
			}else if( an.contentEquals("repair.regen.specification.RefinementAlias")) {
				CtLiteral<String> s = (CtLiteral<String>) ann.getAllValues().get("value");
				handleAlias(s.getValue(), element);
			}
		}
		if(ref.isPresent()) 
			constr = Optional.of(new Predicate(ref.get()));

		return constr;
	}


	
	abstract protected void getGhostFunction(String value, CtElement element);

	abstract protected void handleAlias(String value, CtElement element);
	
	Constraint getRefinement(CtElement elem) {
		return (Constraint)elem.getMetadata(REFINE_KEY);
	}
	
	
}
