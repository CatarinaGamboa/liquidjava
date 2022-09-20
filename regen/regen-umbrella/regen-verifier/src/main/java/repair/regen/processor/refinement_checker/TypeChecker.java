package repair.regen.processor.refinement_checker;

import static org.junit.Assert.fail;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import repair.regen.errors.ErrorEmitter;
import repair.regen.errors.ErrorHandler;
import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.EqualsPredicate;
import repair.regen.processor.constraints.InvocationPredicate;
import repair.regen.processor.constraints.LiteralPredicate;
import repair.regen.processor.constraints.Predicate;
import repair.regen.processor.context.AliasWrapper;
import repair.regen.processor.context.Context;
import repair.regen.processor.context.GhostFunction;
import repair.regen.processor.context.GhostState;
import repair.regen.processor.context.RefinedVariable;
import repair.regen.processor.facade.AliasDTO;
import repair.regen.processor.facade.GhostDTO;
import repair.regen.rj_language.ParsingException;
import repair.regen.rj_language.RefinementsParser;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtNewArray;
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
	public final String TARGET_KEY = "target";
	//	public final String STATE_KEY = "state";
	public final String THIS = "this";
	public final String WILD_VAR = "_";
	public final String freshFormat = "#fresh_%d";
	public final String instanceFormat = "#%s_%d";
	public final String thisFormat = "this#%s";
	public String[] implementedTypes = {"boolean", "int", "short", "long", "float","double", "int[]"}; //TODO add types

	Context context;
	Factory factory;
	VCChecker vcChecker;
	ErrorEmitter errorEmitter;

	public TypeChecker(Context c, Factory fac, ErrorEmitter errorEmitter) {
		this.context = c;
		this.factory = fac;
		this.errorEmitter = errorEmitter;
		vcChecker = new VCChecker(errorEmitter);
	}

	public Context getContext() {
		return context;
	}
	public Factory getFactory() {
		return factory;
	}

	public Constraint getRefinement(CtElement elem) {
		Constraint c = (Constraint)elem.getMetadata(REFINE_KEY);
		return c == null? new Predicate() : c;
	}


	public Optional<Constraint> getRefinementFromAnnotation(CtElement element) throws ParsingException {
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
		if(ref.isPresent()) {
			Predicate p = new Predicate(ref.get(), element, errorEmitter);
			if(errorEmitter.foundError()) return Optional.empty();
			constr = Optional.of(p);
		}
		return constr;
	}

	public void handleStateSetsFromAnnotation(CtElement element) {
		int set = 0;
		for(CtAnnotation<? extends Annotation> ann :element.getAnnotations()) { 
			String an = ann.getActualAnnotation().annotationType().getCanonicalName();
			if(an.contentEquals("repair.regen.specification.StateSet")) {
				set++;
				createStateSet((CtNewArray<String>)ann.getAllValues().get("value"), set, element);
			}
			if(an.contentEquals("repair.regen.specification.Ghost")) {
				CtLiteral<String> s = (CtLiteral<String>)ann.getAllValues().get("value"); 
				createStateGhost(s.getValue(), ann, an, element);
			}	
		}
	}


	private void createStateSet(CtNewArray<String> e,  int set, CtElement element) {
		Optional<GhostFunction> og = createStateGhost(set, element);
		if(!og.isPresent()) {
			System.out.println("Error in creation of GhostFunction");	
			fail();
		}
		GhostFunction g = og.get();
		context.addGhostFunction(g);
		context.addGhostClass(g.getParentClassName());


		List<CtExpression<?>> ls = e.getElements();
		InvocationPredicate ip = new InvocationPredicate(getErrorEmitter(), g.getName(), THIS);
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

	private void createStateGhost(String string, CtAnnotation<? extends Annotation> ann, String an, CtElement element) {
		GhostDTO gd = null;
		try {
			gd = RefinementsParser.getGhostDeclaration(string);
		} catch (ParsingException e) {
			ErrorHandler.printCostumeError(ann, "Could not parse the Ghost Function"+e.getMessage(), errorEmitter);
			return;
		}
		if(gd.getParam_types().size() > 0) {
			ErrorHandler.printCostumeError(ann, "Ghost States have the class as parameter "
					+ "by default, no other parameters are allowed in '"+string+"'", errorEmitter);
			return;
		}
		//Set class as parameter of Ghost
		String qn = getQualifiedClassName(element);
		String sn = getSimpleClassName(element);
		context.addGhostClass(sn);
		List<CtTypeReference<?>> param = Arrays.asList(factory.Type().createReference(qn));
		
		CtTypeReference r = factory.Type().createReference(gd.getReturn_type());
		GhostState gs = new GhostState(gd.getName(), param, r, qn);
		context.addToGhostClass(sn, gs);
	}

	protected String getQualifiedClassName(CtElement element) {
		if(element.getParent() instanceof CtClass<?>) {
			return ((CtClass<?>) element.getParent()).getQualifiedName();
		}else if(element instanceof CtClass<?>) {
			return ((CtClass<?>) element).getQualifiedName();
		}
		return null;
	}

	protected String getSimpleClassName(CtElement element) {
		if(element.getParent() instanceof CtClass<?>) {
			return ((CtClass<?>) element.getParent()).getSimpleName();
		}else if(element instanceof CtClass<?>) {
			return ((CtClass<?>) element).getSimpleName();
		}
		return null;

	}


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
			return Optional.of(gh);
		}
		return Optional.empty();
	}

	protected void getGhostFunction(String value, CtElement element) {
		try {
			GhostDTO f = RefinementsParser.getGhostDeclaration(value);
			if(f != null && element.getParent() instanceof CtClass<?>) {
				CtClass<?> klass =(CtClass<?>) element.getParent(); 
				GhostFunction gh = new GhostFunction(f, factory, klass.getQualifiedName(), klass.getSimpleName()); 
				context.addGhostFunction(gh);
			}
		} catch (ParsingException e) {
			ErrorHandler.printCostumeError(element, "Could not parse the Ghost Function"+e.getMessage(), errorEmitter);
			//	e.printStackTrace();
			return;
		} 
	}

	protected void handleAlias(String value, CtElement element) {	
		try {		
			AliasDTO a = RefinementsParser.getAliasDeclaration(value);
			
			if(a != null) {
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
					AliasWrapper aw = new AliasWrapper(a, factory, WILD_VAR, context, klass, path);
					context.addAlias(aw);
				}	
			}
		} catch (ParsingException e) {
			ErrorHandler.printCostumeError(element, e.getMessage(), errorEmitter);
			return;
//			e.printStackTrace();
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

	public void checkVariableRefinements(Constraint refinementFound, String simpleName, 
			CtTypeReference type, CtElement usage, CtElement variable) throws ParsingException {
		Optional<Constraint> expectedType = getRefinementFromAnnotation(variable);
		Constraint cEt;
		RefinedVariable mainRV = null;
		if(context.hasVariable(simpleName))
			mainRV =  context.getVariableByName(simpleName);

		if(context.hasVariable(simpleName)  && 
				!context.getVariableByName(simpleName).getRefinement().isBooleanTrue())
			cEt = mainRV.getMainRefinement();
		else if(expectedType.isPresent())
			cEt = expectedType.get();
		else
			cEt = new Predicate();

		cEt = cEt.substituteVariable(WILD_VAR, simpleName);
		Constraint cet = cEt.substituteVariable(WILD_VAR, simpleName);

		String newName = String.format(instanceFormat, simpleName, context.getCounter());
		Constraint correctNewRefinement = refinementFound.substituteVariable(WILD_VAR, newName);
		correctNewRefinement = correctNewRefinement.substituteVariable(THIS, newName);
		cEt = cEt.substituteVariable(simpleName, newName);

		
		//Substitute variable in verification
		RefinedVariable rv= context.addInstanceToContext(newName, type, 
				correctNewRefinement, usage);
		for(CtTypeReference t: mainRV.getSuperTypes())
			rv.addSuperType(t);
		context.addRefinementInstanceToVariable(simpleName, newName);
		//smt check
		checkSMT(cEt, usage);//TODO CHANGE
		context.addRefinementToVariableInContext(simpleName,type , cet, usage);
	}
	
	public void checkSMT(Constraint expectedType, CtElement element) {
		vcChecker.processSubtyping(expectedType, context.getGhostState(), 
				WILD_VAR, THIS, element, factory);
		element.putMetadata(REFINE_KEY, expectedType);	
	}
	
	public void checkStateSMT(Constraint prevState, Constraint expectedState, CtElement target, String string) {
		vcChecker.processSubtyping(prevState, expectedState, context.getGhostState(), 
				WILD_VAR, THIS, target, string, factory);
	}

	public boolean checksStateSMT(Constraint prevState, Constraint expectedState, CtElement target) {
		return vcChecker.canProcessSubtyping(prevState, expectedState, context.getGhostState(), 
				WILD_VAR, THIS, target, factory);
	}
	
	public void createError(CtElement element, Constraint expectedType, Constraint foundType, String customeMessage) {
		 vcChecker.printSubtypingError(element, expectedType, foundType,customeMessage); 
	}
	public void createSameStateError(CtElement element, Constraint expectedType, String klass) {
		 vcChecker.printSameStateError(element, expectedType,klass); 
	}
	
	public void createStateMismatchError(CtElement element, String method, Constraint c, String states) {
		vcChecker.printStateMismatchError(element, method, c, states);
	}

	public ErrorEmitter getErrorEmitter() {
		return errorEmitter;
	}

}
