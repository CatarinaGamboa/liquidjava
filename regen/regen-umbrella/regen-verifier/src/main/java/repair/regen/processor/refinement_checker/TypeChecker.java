package repair.regen.processor.refinement_checker;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import repair.regen.language.alias.Alias;
import repair.regen.language.function.FunctionDeclaration;
import repair.regen.language.parser.RefinementParser;
import repair.regen.language.parser.SyntaxException;
import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.EqualsPredicate;
import repair.regen.processor.constraints.Implication;
import repair.regen.processor.constraints.InvocationPredicate;
import repair.regen.processor.constraints.LiteralPredicate;
import repair.regen.processor.constraints.OperationPredicate;
import repair.regen.processor.constraints.Predicate;
import repair.regen.processor.context.AliasWrapper;
import repair.regen.processor.context.Context;
import repair.regen.processor.context.GhostFunction;
import repair.regen.processor.context.GhostState;
import repair.regen.processor.context.RefinedVariable;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.code.CtNewArray;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.CtScanner;

public abstract class TypeChecker extends CtScanner{
	public final String REFINE_KEY = "refinement";
	public final String TARGET_KEY = "target";
	//	public final String STATE_KEY = "state";
	public final String THIS = "this";
	public final String WILD_VAR = "_";
	public final String freshFormat = "#fresh_%d";
	public final String instanceFormat = "#%s_%d";
	public final String thisFormat = "this#%s";
	String[] implementedTypes = {"boolean", "int", "short", "long", "float","double", "int[]"}; //TODO add types

	Context context;
	Factory factory;
	VCChecker vcChecker = new VCChecker();

	public TypeChecker(Context c, Factory fac) {
		this.context = c;
		this.factory = fac;
	}


	Constraint getRefinement(CtElement elem) {
		return (Constraint)elem.getMetadata(REFINE_KEY);
	}


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

	public void handleStateSetsFromAnnotation(CtElement element) {
		int set = 0;
		for(CtAnnotation<? extends Annotation> ann :element.getAnnotations()) { 
			String an = ann.getActualAnnotation().annotationType().getCanonicalName();
			set++;
			if(an.contentEquals("repair.regen.specification.StateSet")) {
				createStateSet((CtNewArray<String>)ann.getAllValues().get("value"), an, set, element);
			}	
		}
	}

	private void createStateSet(CtNewArray<String> e, String an, int set, CtElement element) {
		Optional<GhostFunction> og = createStateGhost(set, element);
		if(!og.isPresent()) {
			System.out.println("Error in creation of GhostFunction");
			System.exit(8);
		}
		GhostFunction g = og.get();
		context.addGhostFunction(g);
		context.addGhostClass(g.getParentClassName());
		
		
		List<CtExpression<?>> ls = e.getElements();
		InvocationPredicate ip = new InvocationPredicate(g.getName(), THIS);
		int order = 0;
		for(CtExpression<?> ce : ls) {
			if(ce instanceof CtLiteral<?>) {
				CtLiteral<String> s = (CtLiteral<String>)ce;
				String f = s.getValue();
				GhostState gs = new GhostState(f, g.getParametersTypes(), 
						factory.Type().BOOLEAN_PRIMITIVE, g.getParentClassName());
				gs.setGhostParent(g);
				gs.setRefinement(/*new OperationPredicate(new InvocationPredicate(f, THIS), "<-->", */
						new EqualsPredicate(ip, LiteralPredicate.getIntPredicate(order))); // open(THIS) -> state1(THIS) == 1
				context.addToGhostClass(g.getParentClassName(), gs);
			}
			order++;
		}
		
	}


	Optional<String> getExternalRefinement(CtInterface<?> intrface) {
		Optional<String> ref = Optional.empty();
		for(CtAnnotation<? extends Annotation> ann :intrface.getAnnotations()) 
			if( ann.getActualAnnotation().annotationType().getCanonicalName()
					.contentEquals("repair.regen.specification.ExternalRefinementsFor")) {
				CtLiteral<String> s = (CtLiteral<String>) ann.getAllValues().get("value");
				ref = Optional.of(s.getValue());
			}		
		return ref;
	}

//
//	protected Optional<GhostFunction> createGhostFunction(String value, int set, int order, CtElement element){
//		CtClass<?> klass = null; 
//
//		if(element.getParent() instanceof CtClass<?>) {
//			klass =(CtClass<?>) element.getParent();
//		}else if(element instanceof CtClass<?>) {
//			klass = (CtClass<?>) element;
//		}
//		if(klass != null) {
//			CtTypeReference<?> ret = factory.Type().BOOLEAN_PRIMITIVE;
//			List<String> params = Arrays.asList(klass.getSimpleName());
//			GhostFunction gh = new GhostFunction(value, params, ret , factory, 
//					klass.getQualifiedName(), klass.getSimpleName(), set, order); 
//			System.out.println(gh.toString());
//			return Optional.of(gh);
//		}
//		return Optional.empty();
//	}
	
	protected Optional<GhostFunction> createStateGhost(int order, CtElement element){
		CtClass<?> klass = null; 

		if(element.getParent() instanceof CtClass<?>) {
			klass =(CtClass<?>) element.getParent();
		}else if(element instanceof CtClass<?>) {
			klass = (CtClass<?>) element;
		}
		if(klass != null) {
			CtTypeReference<?> ret = factory.Type().INTEGER_PRIMITIVE;
			List<String> params = Arrays.asList(klass.getSimpleName());
			GhostFunction gh = new GhostFunction(String.format("%s_state%d", klass.getSimpleName().toLowerCase(), order), 
					params, ret , factory, 
					klass.getQualifiedName(), klass.getSimpleName()); 
			System.out.println(gh.toString());
			return Optional.of(gh);
		}
		return Optional.empty();
	}

	protected void getGhostFunction(String value, CtElement element) {
		try {
			Optional<FunctionDeclaration> ofd = 
					RefinementParser.parseFunctionDecl(value);
			if(ofd.isPresent() && element.getParent() instanceof CtClass<?>) {
				CtClass<?> klass =(CtClass<?>) element.getParent(); 
				GhostFunction gh = new GhostFunction(ofd.get(), factory, klass.getQualifiedName(), klass.getSimpleName()); 
				context.addGhostFunction(gh);
				System.out.println(gh.toString());
			}

		} catch (SyntaxException e) {
			System.out.println("Ghost Function not well written");//TODO REVIEW MESSAGE
			e.printStackTrace();
		}
	}

	protected void handleAlias(String value, CtElement element) {
		try {
			Optional<Alias> oa = RefinementParser.parseAlias(value);
			if(oa.isPresent()) {
				String klass = null;
				String path = null;
				if(element instanceof CtClass) {
					klass = ((CtClass<?>) element).getSimpleName();
					path = ((CtClass<?>) element).getQualifiedName();
				}else if(element instanceof CtInterface<?>) {
					klass = ((CtInterface<?>) element).getSimpleName();
					path = ((CtInterface<?>) element).getQualifiedName();
				}
				if(klass != null && path != null) {
					AliasWrapper a = new AliasWrapper(oa.get(), factory, WILD_VAR, context, klass, path);
					context.addAlias(a);
				}	
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}



	void checkSMT(Constraint expectedType, CtElement element) {
		vcChecker.processSubtyping(expectedType, context.getGhostState(), element);
		element.putMetadata(REFINE_KEY, expectedType);	
	}

	protected boolean checkStateSMT(Constraint prevState, Constraint expectedState, CtElement target) {
		return vcChecker.processSubtyping(prevState, expectedState, context.getGhostState(), target);
	}

	void checkVariableRefinements(Constraint refinementFound, String simpleName, 
			CtTypeReference type, CtElement variable) {
		Optional<Constraint> expectedType = getRefinementFromAnnotation(variable);
		Constraint cEt;
		RefinedVariable mainRV = null;
		if(context.hasVariable(simpleName))
			mainRV =  context.getVariableByName(simpleName);

		if(expectedType.isPresent())
			cEt = expectedType.get();
		else if(context.hasVariable(simpleName))
			cEt = mainRV.getMainRefinement();
		else
			cEt = new Predicate();

		cEt = cEt.substituteVariable(WILD_VAR, simpleName);
		Constraint cet = cEt.substituteVariable(WILD_VAR, simpleName);

		String newName = String.format(instanceFormat, simpleName, context.getCounter());
		Constraint correctNewRefinement = refinementFound.substituteVariable(WILD_VAR, newName);
		correctNewRefinement = correctNewRefinement.substituteVariable(THIS, newName);
		cEt = cEt.substituteVariable(simpleName, newName);

		//Substitute variable in verification
		RefinedVariable rv= context.addInstanceToContext(newName, type, correctNewRefinement);
		for(CtTypeReference t: mainRV.getSuperTypes())
			rv.addSuperType(t);
		context.addRefinementInstanceToVariable(simpleName, newName);
		//smt check
		checkSMT(cEt, variable);//TODO CHANGE
		context.addRefinementToVariableInContext(simpleName,type , cet);
	}
}
